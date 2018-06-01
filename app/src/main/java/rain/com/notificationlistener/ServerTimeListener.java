package rain.com.notificationlistener;

public interface ServerTimeListener {
    void onSuccess(Long response);

    void onFailure(String failureMsg);
}
