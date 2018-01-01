package connection;

public interface ConnectionCallback {
    void onConnected(Message msg);
    void onWrongInfo();
    void onUserAlreadyLogedIn ();
    void onConnectionFailed();
    void onSignUpFailed();
    void onUserChangeStatus(String username, String nickname, Status status);
    void onNewUserConnected (String username, String nickname, Status status);
    void onChangeInfoSucceeded(Message msg);
    void onChangeInfoFailed(Message msg);
    void onReceivedMessage(Message msg);
    void onRequestCall(Message msg);
    void onRespondCallAccept(Message msg);
    void onRespondCallDecline(Message msg);
    void onReceivedVoiceCall(Message msg);
}
