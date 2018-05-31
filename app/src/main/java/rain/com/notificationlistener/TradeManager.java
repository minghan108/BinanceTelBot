package rain.com.notificationlistener;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by MSI\mliu on 30/05/18.
 */

public class TradeManager {
    String TAG = "TradeManager";

    public synchronized void sendTradeRequest(final TradeListener tradeListener){
        SafeThread sendAddBookingRequestThread = new SafeThread("sendAddBookingRequestThread") {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void runSafe() {
                TradeListener listener = tradeListener;
                final Semaphore sem = new Semaphore();
                sem.sem_open();
                OnOkhttpProcessFinish httpListener = new OnOkhttpProcessFinish() {
                    @Override
                    public void onHttpEvent(String response) {
                        handleTradeResponseFromServer(response, tradeListener);
                        sem.sem_post();
                    }

                    @Override
                    public void onHttpFailure(String response) {
                        Log.d(TAG, "sendAddBookingRequest onFailure");
                        handleOnFailure(response, tradeListener);
                        sem.sem_post();
                    }
                };
                (new OkHttpConnection()).getResponse(getTradeUrl(), httpListener, getTradeHttpXmlBody());
                sem.sem_wait();
            }
        };

        sendAddBookingRequestThread.start();


    }

    public synchronized void sendAccountInfoRequest(final TradeListener tradeListener){
        SafeThread sendAddBookingRequestThread = new SafeThread("sendAddBookingRequestThread") {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void runSafe() {
                TradeListener listener = tradeListener;
                final Semaphore sem = new Semaphore();
                sem.sem_open();
                OnOkhttpProcessFinish httpListener = new OnOkhttpProcessFinish() {
                    @Override
                    public void onHttpEvent(String response) {
                        handleTradeResponseFromServer(response, tradeListener);
                        sem.sem_post();
                    }

                    @Override
                    public void onHttpFailure(String response) {
                        Log.d(TAG, "sendAddBookingRequest onFailure");
                        handleOnFailure(response, tradeListener);
                        sem.sem_post();
                    }
                };
                (new OkHttpConnection()).getResponse(getAccountInfoUrl(), httpListener, getTradeHttpXmlBody(), "GET");
                sem.sem_wait();
            }
        };

        sendAddBookingRequestThread.start();


    }

    public static String encode(String secretKey, String queryString) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return new String(Hex.encodeHex(sha256_HMAC.doFinal(queryString.getBytes("UTF-8"))));
    }

    //        try {
//            Log.i(TAG, encode(key, queryString));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    private String getAccountInfoUrl(String recvWindow, String timestamp, String signature) {
        return "https://api.binance.com/api/v3/account?recvWindow=" + recvWindow + "&timestamp=" + timestamp +
                "&signature=" + signature;
    }

    private String getTradeHttpXmlBody() {
        return "";
    }

    private String getTradeUrl() {
        return "";
    }

    private void handleTradeResponseFromServer(String response, TradeListener tradeListener) {
    }

    private void handleOnFailure(String response, TradeListener tradeListener) {
    }
}

