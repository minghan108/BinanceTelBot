package rain.com.notificationlistener;

/**
 * Created by MSI\mliu on 30/05/18.
 */

public interface OnOkhttpProcessFinish {

    void onHttpEvent(String response);

    void onHttpFailure(String response);
}
