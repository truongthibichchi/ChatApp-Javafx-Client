package UI;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String pass;
    private String nickname;

    public User(String username, String pass){
        this.username=username;
        this.pass=pass;
    }


    public User(String username, String pass, String nickname){
        this.username=username;
        this.pass=pass;
        this.nickname=nickname;
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


}
