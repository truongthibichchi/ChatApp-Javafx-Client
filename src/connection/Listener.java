package connection;


import controller.ChatController;
import controller.LogInController;
import controller.MainWindowController;
import controller.SignUpController;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Listener implements Runnable{
    private int port;
    private String hostname;
    private Socket socket;
    private User user;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private ConnectionCallback callback;
    private LogInController logInController;



    private MainWindowController mainWindowController;
    private SignUpController signUpController;

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public User getUser() {return user; }

    public void setSignUpController(SignUpController signUpController) {
        this.signUpController = signUpController;
    }

    public void setLogInController(LogInController logInController) {
        this.logInController = logInController;
    }

    private HashMap<ObservableList<User>, ChatController> conversationControllers = new HashMap<>();

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
    public void setConnectionCallback (ConnectionCallback callback) {
        this.callback = callback;
    }

    public void setConversationController (ObservableList<User> userList, ChatController controller) {
        conversationControllers.put(userList, controller);
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
                            callback.onConnected(msg);
                            break;
                        case LOG_IN_FAILED:
                            callback.onLoginFailed(msg);
                            break;
                        case NEW_USER_CONNECTED:
                            callback.onNewUserConnected(msg);

                        case DISCONNECT:
                            callback.onUserDisconnected(msg);
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
            if (callback != null) {
                callback.onConnectionFailed();
            }
        }
    }
}


