package connection.MessageContent;

import java.io.Serializable;

public class UserLogInMsgContent implements Serializable {
    private String username;
    private String pass;;

    public UserLogInMsgContent(String username, String pass){
        this.username=username;
        this.pass=pass;
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
}
