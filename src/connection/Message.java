package connection;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    private String userName;
    private String pass;
    private String nickname;
    private byte[] avatar;
    private Status status;

    private String text;
    private byte[] voiceMsg;

    private MessageType type;
    private  ArrayList<User> userListData;
    private ArrayList<User> chatUsers;

    public Message(){};
    public Message(String userName, String nickname, MessageType type) {
        this.userName = userName;
        this.nickname = nickname;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public ArrayList<User> getUserListData() {
        return userListData;
    }

    public void setUserListData(ArrayList<User> userListData) {
        this.userListData = userListData;
    }

    public ArrayList<User> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ArrayList<User> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public byte[] getVoiceMsg() {
        return voiceMsg;
    }

    public void setVoiceMsg(byte[] voiceMsg) {
        this.voiceMsg = voiceMsg;
    }
}
