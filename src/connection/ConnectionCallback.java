package connection;

public interface ConnectionCallback {
    void onConnected(Message msg);
    void onLoginFailed(Message msg);
    void onConnectionFailed();
    void onSignUpFailed(Message msg);
    void onUserDisconnected(Message msg);
    void onNewUserConnected (Message msg);
}
