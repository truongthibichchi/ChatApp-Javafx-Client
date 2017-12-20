package UI;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.util.List;

public class ChatWindowController extends StageSceneController{
    @FXML private ImageView imgClose, imgSetting, imgAudio, imgAvatar;
    @FXML private ListView lvUserList, lvChatMessage;
    @FXML private TextArea txtType;
    @FXML private Button btnSend;
    @FXML private JFXComboBox ccbState;
    @FXML private Label lblUsername;



    private Listener listener;
    public void btnCloseAction (){
        Platform.exit();
        System.exit(0);
    }


    public void setListener(Listener listener){this.listener=listener;}
    public void setOnlineUserList(){

    }


}
