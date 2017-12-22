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
                        case LOGIN_SUCCEEDED:
                            user.setNickname(msg.getNickname());
                            callback.onLoginSucceeded(msg);
                            break;


                    }
                }
            }
        }catch (Exception e){

        }
    }

    private void connectToServer() {
        Message msg = new Message(user.getUsername(), user.getPass(), MessageType.LOGIN);
        try {
            outputStream.writeObject(msg);
        } catch (IOException e) {
            if (callback != null) {
                callback.onConnectionFailed();
            }
        }
    }

    synchronized public void sendToServer (Message msg) {
        try {
            outputStream.writeObject(msg);
        } catch (IOException e) {
            if (callback != null) {
                callback.onConnectionFailed ();
            }
        }
    }
}


