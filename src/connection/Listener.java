package connection;


import controller.ChatController;
import controller.MainWindowController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Listener implements Runnable{
    private int port;
    private String hostname;
    private Socket socket;
    private User user;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public MainWindowController mainWindowController;
    public ChatController chatController;

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
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

        }catch (Exception e){
            System.err.println(e);
        }

        try{
            connectToServer();
            inputStream = new ObjectInputStream(socket.getInputStream());
            while(socket.isConnected()){
                Message msg = (Message) inputStream.readObject();

                if(msg!=null){
                    switch (msg.getType()){
                        case LOGIN_SUCCEEDED:
                            mainWindowController.drawUser(user);
                            mainWindowController.setUserList();
                            break;
                    }
                }
            }
        }catch (Exception e){

        }
    }

    private  void connectToServer() throws IOException{
        Message msg = new Message(user.getUsername(), user.getNickname(), MessageType.LOGIN);
        outputStream.writeObject(msg);
    }
}


