package connection.MessageContent;

import java.io.Serializable;

public class UserSignUpMsgContent implements Serializable{
    private String username;
    private String pass;
    private String nickname;
    private String email;

    public UserSignUpMsgContent(String username, String pass, String nickname, String email){
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
