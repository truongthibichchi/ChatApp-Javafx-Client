package UI;


import UI.LogInController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import UI.User;
import connection.MessageContent.UserLogInMsgContent;
import connection.MessageType;
import connection.NetworkMessage;

import java.net.Socket;

public class Listener implements Runnable {
    private int port;
    private String hostname;
    private Socket socket;
    private User user;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private LogInController controller;

    public Listener(String hostname, int port, User user) {
        this.hostname = hostname;
        this.port = port;
        this.user = user;
    }

    public void run() {
        try {
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
//                NetworkMessage msg = new NetworkMessage();
//                UserLogInMsgContent user = new UserLogInMsgContent(username, password);
//                msg.setContent(user);
//                msg.setType(MessageType.LOG_IN);
//                outputStream.writeObject(msg);
        } catch (IOException e) {
            controller = new LogInController();
            controller.showErrorDialog("Could not connect to server");
        }

        try {
            UserLogInMsgContent userContent = new UserLogInMsgContent(user.getUsername(), user.getPass());
            NetworkMessage msg = new NetworkMessage();
            msg.setContent(userContent);
            msg.setType(MessageType.LOG_IN);
            outputStream.writeObject(msg);
            while (socket.isConnected()) {
                NetworkMessage msgReceived = null;
                msgReceived = (NetworkMessage) inputStream.readObject();
                if (msgReceived != null) {
                    if (msgReceived.getType() == MessageType.LOG_IN) {
                        LogInController con = new LogInController();
                        con.LoadChatForm(user.getUsername(), user.getPass());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

//    public void LogIn(User user) {
//        Thread thread = new Thread(() -> {
//            try {
//                socket = new Socket(hostname, port);
//                outputStream = new ObjectOutputStream(socket.getOutputStream());
//                inputStream = new ObjectInputStream(socket.getInputStream());
////                NetworkMessage msg = new NetworkMessage();
////                UserLogInMsgContent user = new UserLogInMsgContent(username, password);
////                msg.setContent(user);
////                msg.setType(MessageType.LOG_IN);
////                outputStream.writeObject(msg);
//            } catch (IOException e) {
//                controller= new LogInController();
//                controller.showErrorDialog("Could not connect to server");
//            }
//
//        });
//        thread.start();
//        try{
//            NetworkMessage msg = new NetworkMessage();
//            UserLogInMsgContent userContent = new UserLogInMsgContent(user.getUsername(), user.getPass());
//            msg.setContent(userContent);
//            msg.setType(MessageType.LOG_IN);
//            outputStream.writeObject(msg);
//            while(socket.isConnected()){
//                NetworkMessage msgReceived = null;
//                inputStream = new ObjectInputStream(socket.getInputStream());
//                msgReceived = (NetworkMessage) inputStream.readObject();
//                if(msgReceived!=null){
//                    if(msgReceived.getType()==MessageType.LOG_IN){
//                        LogInController con = new LogInController();
//                        con.LoadChatForm(user.getUsername(), user.getPass());
//                    }
//                }
//            }
//        }catch (IOException|ClassNotFoundException e){
//            e.printStackTrace();
//        }
//        //LogInController con = new LogInController();
//        //con.LoadChatForm(user.getUsername(), user.getPass());
//    }




    //    private Socket socket;
//    private int port;
//    public String hostname;
//    public static MessageType msgType;
//    public static User user;
//    public static ChatWindowController chatWindowController;
//    public static LogInController logInController;
//    private static ObjectOutputStream output;
//    private OutputStream os;
//    private InputStream is;
//    private ObjectInputStream input;
//
//    public Listener(String hostname, int port, User user){
//        this.hostname=hostname;
//        this.port=port;
//        this.user=user;
//    }
//
//    public void run(){
//        try{
//            socket = new Socket(hostname, port);
//            while(true) {
//               // LogInController.getInstance().showScene();
//                os = socket.getOutputStream();
//                output = new ObjectOutputStream(os);
//                is = socket.getInputStream();
//                input = new ObjectInputStream(is);
//            }
//        }catch (IOException e){
//            logInController.getInstance().showErrorDialog("Could not connect to server");
//        }
//        try{
//            connectToserver();
//            while(socket.isConnected()){
//                NetworkMessage msg=null;
//                msg=(NetworkMessage) input.readObject();
//                if(msg!=null){
//                    if(msg.getType()==MessageType.LOG_IN) {
//                        user = (User) msg.getContent();
//                        logInController.LogIn(user.getUsername(), user.getPass());
//                    }
//
//                }
//            }
//        }catch (IOException | ClassNotFoundException e){
//            e.printStackTrace();
//        }
//    }
//
//    public  void connectToserver() throws IOException{
//        NetworkMessage msg = new NetworkMessage();
//        msg.setContent(user);
//        msg.setType(MessageType.LOG_IN);
//        output.writeObject(msg);
//    }


