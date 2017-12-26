package connection;

public interface ConnectionCallback {
    void onConnected(Message msg);
    void onLoginFailed(Message msg);
    void onConnectionFailed();
    void onSignUpFailed(Message msg);
    void onUserDisconnected(String username, String nickname, Status status);
    void onNewUserConnected (String username, String nickname, Status status);
}
