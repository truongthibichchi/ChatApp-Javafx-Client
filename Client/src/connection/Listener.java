package connection;


import UI.ChatWindowController;
import UI.LogInController;

import java.io.*;
import java.net.Socket;
import pojo.*;

public class Listener implements Runnable{
    private Socket socket;
    private int port;
    public String hostname;
    public static MessageType msgType;
    public static Users user;
    public static ChatWindowController chatWindowController;
    public static Object controller;
    private static ObjectOutputStream output;
    private OutputStream os;
    private InputStream is;
    private ObjectInputStream input;

    public Listener(String hostname, int port, Users user){
        this.hostname=hostname;
        this.port=port;
        this.user=user;
    }

    public void run(){
        try{
            socket = new Socket(hostname, port);
            while(true) {
               // LogInController.getInstance().showScene();
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);
                is = socket.getInputStream();
                input = new ObjectInputStream(is);

            }
        }catch (IOException e){
            LogInController.getInstance().showErrorDialog("Could not connect to server");
        }
        try{
            connectToserver();
            while(socket.isConnected()){
                Message msg=null;
                msg=(Message) input.readObject();
                if(msg!=null){
                    if(msg.getType()==MessageType.CONNECTED) {
                        chatWindowController=new ChatWindowController();
                        chatWindowController.setOnlineUserList();

                    }

                }
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public  void connectToserver() throws IOException{
        Message msg = new Message();
        msg.setContent(user);
        msg.setType(MessageType.LOG_IN);
        output.writeObject(msg);
    }

}
