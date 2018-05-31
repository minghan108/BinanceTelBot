package rain.com.notificationlistener;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    String TAG = "JsonParser";

    public void parseServerTimeResponse(String response, TradeListener tradeListener) {
        Log.d(TAG, "SeverTimeResponse: " + response);

        try {
            JSONObject obj = new JSONObject(response);

            JSONArray jsonArray = obj.getJSONArray("serverTime");
            Log.d(TAG, "jsonArray length: " + jsonArray.length());
            Log.d(TAG, "serverTime JsonArray: " + jsonArray.get(0));
            jsonArray = (JSONArray)jsonArray.get(0);
            Log.d(TAG, "serverTime: " + jsonArray.get(0));
            Double askPriceDouble = Double.parseDouble(jsonArray.get(0).toString());
            tradeListener.onSuccess(askPriceDouble);


        } catch (JSONException e) {
            tradeListener.onFailure("Failure when parsing SendBuyResponse");
            e.printStackTrace();
        }
    }
}
