package connection;


import controller.LogInController;
import controller.MainWindowController;
import controller.SignUpController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static connection.MessageType.*;

public class Listener implements Runnable {
    private static ObjectOutputStream outputStream;
    private int port;
    private String hostname;
    private Socket socket;
    private User user;
    private ObjectInputStream inputStream;

    private ConnectionCallback connectioncallback;

    private LogInController logInController;
    private SignUpController signUpController;
    private MainWindowController mainWindowController;


    public Listener(String hostname, int port, User user) {
        this.hostname = hostname;
        this.port = port;
        this.user = user;
    }

    public static void sendVoiceMessage(String username, ArrayList<User> users, byte[] audio) throws IOException {
        Message msg = new Message();
        msg.setUserName(username);
        msg.setVoiceMsg(audio);
        msg.setChatUsers(users);
        msg.setType(MessageType.VOICE);
        outputStream.writeObject(msg);
    }

    public static void respondCallAccept(String username, ArrayList<User> users) throws IOException {
        Message msg = new Message();
        msg.setType(RESPOND_CALL_ACCEPT);
        msg.setUserName(username);
        msg.setChatUsers(users);
        outputStream.writeObject(msg);
    }

    public static void respondCallDecline(String username, ArrayList<User> users) throws IOException {
        Message msg = new Message();
        msg.setType(RESPOND_CALL_DECLINE);
        msg.setChatUsers(users);
        msg.setUserName(username);
        outputStream.writeObject(msg);
    }

    public User getUser() {
        return user;
    }

    public void setSignUpController(SignUpController signUpController) {
        this.signUpController = signUpController;
    }

    public void setLogInController(LogInController logInController) {
        this.logInController = logInController;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }


    public void setConnectionCallback(ConnectionCallback callback) {
        this.connectioncallback = callback;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {
            System.err.println(e);
        }

        try {
            connectToServer();

            while (socket.isConnected()) {
                Message msg = (Message) inputStream.readObject();

                if (msg != null) {
                    switch (msg.getType()) {
                        case CONNECTED:
                            connectioncallback.onConnected(msg);
                            break;

                        case WRONG_INFO:
                            connectioncallback.onWrongInfo();
                            break;

                        case ALREADY_LOGGED_IN:
                            connectioncallback.onUserAlreadyLogedIn();
                            break;

                        case SIGN_UP_FAILED:
                            connectioncallback.onSignUpFailed();
                            break;

                        case NEW_USER_CONNECTED:
                            connectioncallback.onNewUserConnected(msg.getUserName(), msg.getNickname(), msg.getStatus());
                            break;

                        case DISCONNECT:
                            connectioncallback.onUserDisconnected(msg.getUserName(), msg.getNickname(), msg.getStatus());
                            break;

                        case CHANGE_INFO_SUCCEEDED:
                            connectioncallback.onChangeInfoSucceeded(msg);
                            break;

                        case CHANGE_INFO_FAILED:
                            connectioncallback.onChangeInfoFailed(msg);
                            break;

                        case CHAT_TEXT:
                            connectioncallback.onReceivedMessage(msg);
                            break;

                        case VOICE:
                            connectioncallback.onReceivedMessage(msg);
                            break;

                        case REQUEST_CALL:
                            connectioncallback.onRequestCall(msg);
                            break;

                        case RESPOND_CALL_ACCEPT:
                            //TODO: Callback BEGIN recoding
                            break;
                        case RESPOND_CALL_DECLINE:
                            //TODO: END recording
                            break;

                        case VOICE_CALL:
                            //TODO: Playback
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void connectToServer() {
        Message msg = new Message();

        //log in
        if (this.logInController != null) {
            msg.setUserName(user.getUsername());
            msg.setPass(user.getPass());
            msg.setType(MessageType.LOGIN);

        }

        //sign up
        if (this.logInController == null && this.signUpController != null) {
            msg.setUserName(user.getUsername());
            msg.setPass(user.getPass());
            msg.setNickname(user.getNickname());
            msg.setAvatar(user.getAvatar());
            msg.setType(MessageType.SIGN_UP);
        }
        sendToServer(msg);
    }

    synchronized public void sendToServer(Message msg) {
        try {
            outputStream.writeObject(msg);
        } catch (IOException e) {
            if (connectioncallback != null) {
                connectioncallback.onConnectionFailed();
            }
        }
    }

    public void changeInfoUserMain(User user) {
        Message msg = new Message();
        msg.setUserName(user.getUsername());
        msg.setPass(user.getPass());
        msg.setNickname(user.getNickname());
        msg.setAvatar(user.getAvatar());
        msg.setType(MessageType.CHANGE_INFO);

        sendToServer(msg);
    }

    public void chatText(String username, ArrayList<User> users, String text) {
        Message msg = new Message();
        msg.setUserName(username);
        msg.setText(text);
        msg.setChatUsers(users);
        msg.setType(MessageType.CHAT_TEXT);
        sendToServer(msg);
    }

    public void requestCall(String username, ArrayList<User> users) {
        Message msg = new Message();
        msg.setUserName(username);
        msg.setChatUsers(users);
        msg.setType(REQUEST_CALL);
        sendToServer(msg);
    }

    public void closeChatWindow(ArrayList<User> users) {
        mainWindowController.closeChatWindow(users);
    }
}


