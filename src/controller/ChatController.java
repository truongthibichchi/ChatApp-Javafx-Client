package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import connection.ChatCallback;
import connection.Listener;
import connection.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import connection.User;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

public class ChatController extends StageSceneController implements ChatCallback{
    @FXML private Pane pane;
    @FXML private ImageView imgCloseConversation, imgAudio;
    @FXML private JFXListView lvChatLine;
    @FXML private ListView lvParticipants;
    @FXML private TextArea txtMess;
    @FXML private JFXButton btnSend;

    private double xOffset;
    private double yOffset;

    private ArrayList<User> users;
    private Listener listener;

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void imgCloseConversationAction(){
        this.stage.hide();

    }

    public void drawUserList(ArrayList<User> users) {
        Platform.runLater(() -> {
            lvParticipants.setItems(FXCollections.observableList(users));
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

    public void btbSendMessgageAction (){
        listener.setChatCallback(this);
        listener.setChatControllers(users, this);
        String text = txtMess.getText();
        ArrayList<User> participants = users;
        listener.chatText(participants, text);
    }

    @Override
    public void onSendTextSuceeded(Message message) {

    }
}
