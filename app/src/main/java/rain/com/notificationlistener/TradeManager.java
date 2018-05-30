package rain.com.notificationlistener;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by MSI\mliu on 30/05/18.
 */

public class TradeManager {
    String TAG = "TradeManager";

    public synchronized void sendTradeRequest(final TradeListener tradeListener, final int programIndex){
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

