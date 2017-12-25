package connection;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String pass;
    private String nickname;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUsername() {

        return username;
    }

    public String getPass() {
        return pass;
    }

    public String getNickname() {
        return nickname;
    }

    public Status getStatus() {
        return status;
    }
}
