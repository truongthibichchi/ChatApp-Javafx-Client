package UI;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import pojo.Users;


public class ChatWindowController extends StageSceneController{
    @FXML private ImageView imgClose, imgSetting, imgAudio, imgAvatar;
    @FXML private ListView lvUserList, lvChatMessage;
    @FXML private TextArea txtType;
    @FXML private Button btnSend;
    @FXML private JFXComboBox ccbState;

    private Users user;

    public void btnCloseAction (){
        Platform.exit();
        System.exit(0);
    }

    public void setUser(Users user){
        this.user=user;
    }

    public void setOnlineUserList(){

    }


}
