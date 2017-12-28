package connection;


import controller.ChatController;
import controller.LogInController;
import controller.MainWindowController;
import controller.SignUpController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Listener implements Runnable{
    private int port;
    private String hostname;
    private Socket socket;
    private User user;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private ConnectionCallback connectioncallback;

    private LogInController logInController;
    private MainWindowController mainWindowController;
    private SignUpController signUpController;

    public User getUser() {return user; }
    public void setSignUpController(SignUpController signUpController) {
        this.signUpController = signUpController;
    }
    public void setLogInController(LogInController logInController) {
        this.logInController = logInController;
    }
    public void setMainWindowController(MainWindowController mainWindowController) {this.mainWindowController = mainWindowController; }
    public void setConnectionCallback (ConnectionCallback callback) {
        this.connectioncallback = callback;
    }


    public Listener(String hostname, int port, User user) {
        this.hostname = hostname;
        this.port = port;
        this.user = user;
    }

    @Override
    public void run() {
        try{
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

        }catch (Exception e){
            System.err.println(e);
        }

        try{
            connectToServer();

            while(socket.isConnected()){
                Message msg = (Message) inputStream.readObject();

                if(msg!=null){
                    switch (msg.getType()){
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
                            connectioncallback.onNewUserConnected(msg.getUserName(),msg.getNickname(), msg.getStatus());
                            break;
                        case DISCONNECT:
                            connectioncallback.onUserDisconnected(msg.getUserName(),msg.getNickname(), msg.getStatus());
                            break;

                        case CHAT_TEXT:
                            connectioncallback.onReCeivedAtextMessage(msg);
                            break;
                    }
                }
            }
        }catch (Exception e){
            System.err.println(e);
        }
    }

    private void connectToServer() {
        Message msg = new Message();

        //log in
        if(this.logInController!=null &&this.signUpController==null){
            msg.setUserName(user.getUsername());
            msg.setPass(user.getPass());
            msg.setType(MessageType.LOGIN);
        }

        //sign up
        if(this.logInController==null && this.signUpController!=null){
            msg.setUserName(user.getUsername());
            msg.setPass(user.getPass());
            msg.setNickname(user.getNickname());
            msg.setType(MessageType.SIGN_UP);
        }
        sendToServer(msg);
    }

    synchronized public void sendToServer (Message msg) {
        try {
            outputStream.writeObject(msg);
        } catch (IOException e) {
            if (connectioncallback != null) {
                connectioncallback.onConnectionFailed();
            }
        }
    }

    public void chatText(String username, ArrayList<User> users, String text){
        Message msg = new Message();
        msg.setUserName(username);
        msg.setText(text);
        msg.setChatUsers(users);
        msg.setType(MessageType.CHAT_TEXT);
        sendToServer(msg);
    }

    public void closeChatWindow(ArrayList<User> users){
        mainWindowController.closeChatWindow(users);
    }
}


