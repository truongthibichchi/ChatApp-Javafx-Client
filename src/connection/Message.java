package connection;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    private String nickname;
    private String userName;
    private String pass;
    private MessageType type;
    private ArrayList<User> userList;

    public void setUserListData(ArrayList<User> userListData) {
        this.userListData = userListData;
    }

    public ArrayList<User> getUserListData() {
        return userListData;
    }

    private  ArrayList<User> userListData;


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public Message(){};
    public Message(String nickname, String userName, MessageType type) {
        this.nickname = nickname;
        this.userName = userName;
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }
    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }
}
