package UI;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.util.List;

public class ChatController extends StageSceneController{
    @FXML private ImageView imgClose, imgAudio, imgAvatar;
    @FXML private ListView lvUserList, lvChatMessage;
    @FXML private JFXTextArea txtType;
    @FXML private Button btnSend;
    @FXML private Label lblUsername;



    private Listener listener;
    public void imgCloseAction (){
        Platform.exit();
        System.exit(0);
    }


    public void setListener(Listener listener){this.listener=listener;}
    public void setOnlineUserList(){

    }


}
