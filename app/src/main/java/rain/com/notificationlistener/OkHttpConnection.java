package rain.com.notificationlistener;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import info.guardianproject.netcipher.NetCipher;

/**
 * Created by MSI\mliu on 30/05/18.
 */

public class OkHttpConnection {
    private final static String TAG = " [OkHttpConnection] ";
    private String responseData = "";
    private OnOkhttpProcessFinish mListener;
    private final static int MAX_CONCURRENCY_HTTP_CONNECTION = 3;
    private static java.util.concurrent.Semaphore httpRequestThreadSemaphore = new java.util.concurrent.Semaphore(MAX_CONCURRENCY_HTTP_CONNECTION);



    public void getResponse(final String Url, OnOkhttpProcessFinish onOkhttpProcessFinishListener, String httpBody, String restCmd){
        try {
            httpRequestThreadSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mListener = onOkhttpProcessFinishListener;
        Log.d(TAG, " Command sent to dongle:" + Url);

//        URL url = null;
//        //HttpsURLConnection.setDefaultSSLSocketFactory(new NoSSLv3Factory());
//        HttpsURLConnection urlConnection = null;

//        URL url = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            url = new URL(Url);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            urlConnection.setRequestProperty("Accept", "application/json");
//            urlConnection.setDefaultUseCaches(false);
//            urlConnection.setUseCaches(false);
//            urlConnection.setRequestMethod(restCmd);
//            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);
//            urlConnection.setInstanceFollowRedirects(false);
//            urlConnection.setConnectTimeout(5000);
//            urlConnection.setReadTimeout(5000);
//            OutputStream output = new BufferedOutputStream(urlConnection.getOutputStream());
//            //output.write(httpHeader.getBytes());
//            output.flush();
//            int responseCode = urlConnection.getResponseCode();
//
//            if(responseCode == HttpURLConnection.HTTP_OK){
//                Log.d(TAG, "getResponse onSuccess: " + responseCode);
//                responseData = readStream(urlConnection.getInputStream());
//                onResponse(responseData);
//            } else {
//                Log.d(TAG, "getResponse onFailure: " + responseCode);
//                onFailure(responseCode);
//            }

        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(Url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDefaultUseCaches(false);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                responseData = readStream(urlConnection.getInputStream());
//                try {
//                    getLogger().info(TAG + "get http response" + responseData);
//                } catch(Exception e) {
//                    getLogger().info(TAG + "output response due to the format issue");
//                }
                onResponse(responseData);
            } else {
                Log.d(TAG, "getResponse onFailure: ");
                onFailure(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "getResponse onFailure IOException: ");
            onFailure(-1);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getResponse onFailure Exception: ");
            onFailure(-1);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


    private void onFailure(int httpErrorCode) {
        httpRequestThreadSemaphore.release();
        Log.d(TAG, "Call On Failure " + httpErrorCode);
        if (mListener != null) {
            mListener.onHttpFailure(String.valueOf(httpErrorCode));
        }
    }


    private void onResponse(String responseData) {
        httpRequestThreadSemaphore.release();
        if (mListener != null) {
            mListener.onHttpEvent(responseData);
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}

