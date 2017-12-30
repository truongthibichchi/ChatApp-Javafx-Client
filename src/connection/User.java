package connection;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String pass;
    private String nickname;
    private byte[] avatar;
    private Status status;

    public User(String username, String pass){
        this.username=username;
        this.pass=pass;
    }


    public User(String username, String pass, String nickname){
        this.username=username;
        this.pass=pass;
        this.nickname=nickname;
    }

    public User(String username, String pass, String nickname, Status status) {
        this.username = username;
        this.pass = pass;
        this.nickname = nickname;
        this.status = status;
    }

    public User(String username, String pass, String nickname, byte[] avatar) {
        this.username = username;
        this.pass = pass;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public User(String username, String pass, String nickname, byte[] avatar, Status status) {
        this.username = username;
        this.pass = pass;
        this.nickname = nickname;
        this.avatar = avatar;
        this.status = status;
    }

    public User(String username, String nickname, Status status) {
        this.username = username;
        this.nickname = nickname;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
