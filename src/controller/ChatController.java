package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import connection.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController extends StageSceneController{

    @FXML private Pane pane;
    private double xOffset;
    private double yOffset;
    @FXML private ImageView imgCloseConversation, imgAudio;
    @FXML private JFXListView lvChatLine;
    @FXML private ListView lvParticipants;
    @FXML private TextArea txtMess;
    @FXML private JFXButton btnSend;


    private ObservableList<User> participants;
    public void setParticipants(ObservableList<User> participants) {
        this.participants = participants;
    }
    public void imgCloseConversationAction(){
        this.stage.hide();
    }

    public void drawUserList() {
        Platform.runLater(() -> {

            lvParticipants.setItems(participants);
            lvParticipants.setOrientation(Orientation.HORIZONTAL);
            lvParticipants.setCellFactory(new CellRendererInChat());
        });
    }

    public void addDragAndDropHandler() {
        pane.setOnMousePressed(event -> {
            xOffset = this.stage.getX() - event.getScreenX();
            yOffset =this.stage.getY() - event.getScreenY();
            pane.setCursor(Cursor.CLOSED_HAND);
        });

        pane.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() + xOffset);
            this.stage.setY(event.getScreenY() + yOffset);

        });

        pane.setOnMouseReleased(event -> {
            pane.setCursor(Cursor.DEFAULT);
        });
    }
}
