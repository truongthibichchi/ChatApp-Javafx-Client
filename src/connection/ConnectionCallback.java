package connection;

public interface ConnectionCallback {
    void onLoginSucceeded (Message message);
    void onConnectionFailed ();
}
