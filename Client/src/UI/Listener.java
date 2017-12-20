package UI;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import connection.MessageContent.UserLogInMsgContent;
import connection.MessageContent.UserSignUpMsgContent;
import connection.MessageType;
import connection.NetworkMessage;

import java.net.Socket;

public class Listener {
    private int port;
    private String hostname;
    private Socket socket;
    private User user;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public LogInController logIncontroller;
    public SignUpController signUpController;
    public ChatWindowController chatWindowController;

    public Listener(String hostname, int port, User user) {
        this.hostname = hostname;
        this.port = port;
        this.user = user;
    }

//    public void run() {
//        try {
//            socket = new Socket(hostname, port);
//            outputStream = new ObjectOutputStream(socket.getOutputStream());
//            inputStream = new ObjectInputStream(socket.getInputStream());
//        } catch (IOException e) {
//            logIncontroller.showErrorDialog("Could not connect to server");
//        }

//        try {
//           LogIn();
//            while (socket.isConnected()) {
//                NetworkMessage msgReceived = null;
//                msgReceived = (NetworkMessage) inputStream.readObject();
//                if (msgReceived != null) {
//                    if (msgReceived.getType() == MessageType.CONNECTED) {
//                       logIncontroller.LoadChatForm();
//                    }
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }


    private   void LogIn() throws IOException{
        UserLogInMsgContent userContent = new UserLogInMsgContent(user.getUsername(), user.getPass());
        NetworkMessage msg = new NetworkMessage();
        msg.setContent(userContent);
        msg.setType(MessageType.LOG_IN);
        outputStream.writeObject(msg);
    }

    public void SignUp (User user) throws IOException{
        Thread thread = new Thread(()->{
                try {
                    socket = new Socket(hostname, port);
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    inputStream = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                   // logIncontroller.showErrorDialog("Could not connect to server");
                }
                try {
                    UserSignUpMsgContent usercontent = new UserSignUpMsgContent(user.getUsername(), user.getPass(), user.getNickname(), user.getEmail());
                    NetworkMessage msg = new NetworkMessage();
                    msg.setContent(usercontent);
                    msg.setType(MessageType.SIGN_UP);
                    outputStream.writeObject(msg);
                    while (socket.isConnected()) {
                        NetworkMessage msgReceived = null;
                        msgReceived = (NetworkMessage) inputStream.readObject();
                        if (msgReceived != null) {
                            if (msgReceived.getType() == MessageType.CONNECTED) {
                                signUpController.LoadChatForm();
                            }
                        }
                    }
                }catch (Exception e){}

            });
        thread.start();
    }
}


