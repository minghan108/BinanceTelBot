package rain.com.notificationlistener;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    String TAG = "JsonParser";

    public void parseServerTimeResponse(String response, ServerTimeListener serverTimeListener) {
        Log.d(TAG, "SeverTimeResponse: " + response);
        Long serverTime = 0L;

        try {
            JSONObject obj = new JSONObject(response);
            serverTime = obj.getLong("serverTime");
            Log.d(TAG, "serverTime: " + serverTime);
            serverTimeListener.onSuccess(serverTime);


        } catch (JSONException e) {
            serverTimeListener.onFailure("Failure when parsing SendBuyResponse");
            e.printStackTrace();
        }
    }
}
