package rain.com.notificationlistener;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MSI\mliu on 29/05/18.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SimpleNotificationListener extends NotificationListenerService {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public TradeManager tradeManager = new TradeManager();
    public String TAG = "SimpleNotificationListener";
    public enum BaseDropPercentage{
        NO_DROP,
        DROP_FIVE_PERCENT,
        DROP_TEN_PERCENT
    }
    Context context;

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction("rain.com.notificationlistener");


    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        BaseDropPercentage baseDropPercentage;
        String symbolKey = "";
        String symbol = "";
        double base;
        Log.d(TAG, "onNotificationPosted");
        String pack = sbn.getPackageName();
        String ticker ="";
        if(sbn.getNotification().tickerText !=null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = "";
        if (extras.getCharSequence("android.text") != null)
            text = extras.getCharSequence("android.text").toString();
        int id1 = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Bitmap id = sbn.getNotification().largeIcon;


        Log.i("Package",pack);
        Log.i("Ticker",ticker);
        Log.i("Title",title);
        Log.i("Text",text);

        if (ticker.contains("Hodloo Binance 5%")){
            baseDropPercentage = BaseDropPercentage.DROP_FIVE_PERCENT;
            Log.i("baseDropPercentage","BaseDropPercentage.DROP_FIVE_PERCENT");
            symbolKey = "Hodloo Binance 5%";
        } else if (ticker.contains("Hodloo Binance 10%")){
            baseDropPercentage = BaseDropPercentage.DROP_TEN_PERCENT;
            Log.i("baseDropPercentage","BaseDropPercentage.DROP_TEN_PERCENT");
            symbolKey = "Hodloo Binance 10%";
        } else {
            baseDropPercentage = BaseDropPercentage.NO_DROP;
        }

        //String pattern = ".*" + symbolKey + "=(.*?)(?:\\s*\\S+:.*|$)";
        //String pattern = ".*5=(.*?)(?:\\s*\\S+:.*|$)";
        //String pattern = "(?<=Hodloo Binance 5%).*";
        //String pattern = "(\\w*):(\"[^\"]*\"|[^\\s]*)";
        String symbolPatternStr = "(" + symbolKey + "):\\s*(\\S+)";
        Pattern symbolPattern = Pattern.compile(symbolPatternStr);
        Matcher symbolMatcher = symbolPattern.matcher(ticker);
        if (symbolMatcher.find()) {
            symbol = symbolMatcher.group(2);
            symbol = symbol.replaceAll("\\s+","");
            symbol = symbol.replaceAll("/\\z", "");
            Log.i("Regex Symbol", symbol);
        } else {
            Log.i("Regex Symbol", "No Match");
        }

        String basePatternStr = "(Base):\\s*(\\S+)";
        Pattern basePattern = Pattern.compile(basePatternStr);
        Matcher baseMatcher = basePattern.matcher(ticker);
        if (baseMatcher.find()) {
            base = Double.parseDouble(baseMatcher.group(2).replaceAll("\\s+",""));
            //symbol.replaceAll("\\s+","");
            Log.i("Regex Base", String.valueOf(base));
        } else {
            Log.i("Regex Base", "No Match");
        }

        if (baseDropPercentage != BaseDropPercentage.NO_DROP){
            tradeManager.sendServerTimeRequest(new TradeListener() {
                @Override
                public void onSuccess(Double response) {

                }

                @Override
                public void onFailure(String failureMsg) {

                }
            });
        }

//        Intent msgrcv = new Intent("Msg");
//        msgrcv.putExtra("package", pack);
//        msgrcv.putExtra("ticker", ticker);
//        msgrcv.putExtra("title", title);
//        msgrcv.putExtra("text", text);
//        if(id != null) {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            //id.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//            msgrcv.putExtra("icon",byteArray);
//        }
//        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationRemoved");
    }
}
