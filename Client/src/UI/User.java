package UI;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String pass;
    private String nickname;
    private String state;
    private String email;
    private Integer isActived;

    public User(String username, String pass){
        this.username=username;
        this.pass=pass;
    }


    public User(String username, String pass, String nickname, String email){
        this.username=username;
        this.pass=pass;
        this.nickname=nickname;
        this.email=email;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Integer getIsActived() {
        return isActived;
    }

    public void setIsActived(Integer isActived) {
        this.isActived = isActived;
    }
}
