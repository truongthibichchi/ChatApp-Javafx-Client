package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

public class CallController extends StageSceneController{
    @FXML private BorderPane pane;
    @FXML private ImageView imgClose;
    @FXML private Circle cirAvatar;
    @FXML private Label lblNickname, lblNoti;
    @FXML private JFXButton btnAccept, btnDecline;

    public void imgCloseAction(){
        this.stage.close();
    }


}
