package connection;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    private String nickname;
    private String userName;
    private String pass;
    private MessageType type;
    private  ArrayList<User> userListData;

    public Message(){};
    public Message(String userName, String nickname, MessageType type) {
        this.userName = userName;
        this.nickname = nickname;
        this.type = type;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setType(MessageType type) {
        this.type = type;
    }


    public void setUserListData(ArrayList<User> userListData) {
        this.userListData = userListData;
    }

    public String getNickname() {

        return nickname;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }

    public MessageType getType() {
        return type;
    }


    public ArrayList<User> getUserListData() {
        return userListData;
    }
}
