package UI;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    public Listener(String hostname, int port, User user, LogInController con) {
        this.hostname = hostname;
        this.port = port;
        this.user = user;
        this.controller=con;
    }

    public void run() {
        try {
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            controller.showErrorDialog("Could not connect to server");
        }

        try {
           LogIn();
            while (socket.isConnected()) {
                NetworkMessage msgReceived = null;
                msgReceived = (NetworkMessage) inputStream.readObject();
                if (msgReceived != null) {
                    if (msgReceived.getType() == MessageType.CONNECTED) {
                       controller.LoadChatForm();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private   void LogIn() throws IOException{
        UserLogInMsgContent userContent = new UserLogInMsgContent(user.getUsername(), user.getPass());
        NetworkMessage msg = new NetworkMessage();
        msg.setContent(userContent);
        msg.setType(MessageType.LOG_IN);
        outputStream.writeObject(msg);
    }
}


