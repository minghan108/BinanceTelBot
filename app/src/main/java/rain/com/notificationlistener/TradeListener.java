package rain.com.notificationlistener;

/**
 * Created by MSI\mliu on 30/05/18.
 */

public interface TradeListener {
    void onSuccess(Double response);

    void onFailure(String failureMsg);
}
