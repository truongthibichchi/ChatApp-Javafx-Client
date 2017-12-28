package connection;

public interface ConnectionCallback {
    void onConnected(Message msg);
    void onWrongInfo();
    void onUserAlreadyLogedIn ();
    void onConnectionFailed();
    void onSignUpFailed();
    void onUserDisconnected(String username, String nickname, Status status);
    void onNewUserConnected (String username, String nickname, Status status);
    void onReCeivedAtextMessage(Message msg);
}
