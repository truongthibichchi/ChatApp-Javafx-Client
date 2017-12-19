package connection;


import UI.LogInController;
import connection.MessageContent.UserLogInMsgContent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Listener {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private LogInController controller;

    public Listener() {
    }

    public void LogIn(String hostname, int port, String username, String password) {
        Thread thread = new Thread(() -> {
            try {
                socket = new Socket(hostname, port);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                //inputStream = new ObjectInputStream(socket.getInputStream());
                NetworkMessage msg = new NetworkMessage();
                UserLogInMsgContent user = new UserLogInMsgContent(username, password);
                msg.setContent(user);
                msg.setType(MessageType.LOG_IN);
                outputStream.writeObject(msg);
            } catch (IOException e) {
                controller= new LogInController();
                controller.showErrorDialog("Could not connect to server");
            }
            try{
                while(socket.isConnected()){
                    NetworkMessage msg = null;
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    msg = (NetworkMessage) inputStream.readObject();
                    if(msg!=null){
                        if(msg.getType()==MessageType.CONNECTED){
                            LogInController con = new LogInController();
                            con.LoadChatForm(username, password);
                        }
                    }
                }
            }catch (IOException|ClassNotFoundException e){
                e.printStackTrace();
            }
        });
        thread.start();
    }

}


    //    private Socket socket;
//    private int port;
//    public String hostname;
//    public static MessageType msgType;
//    public static UserContent user;
//    public static ChatWindowController chatWindowController;
//    public static LogInController logInController;
//    private static ObjectOutputStream output;
//    private OutputStream os;
//    private InputStream is;
//    private ObjectInputStream input;
//
//    public Listener(String hostname, int port, UserContent user){
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
//                        user = (UserContent) msg.getContent();
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


