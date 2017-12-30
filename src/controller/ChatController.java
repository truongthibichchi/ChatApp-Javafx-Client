package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import connection.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.util.ArrayList;

public class ChatController extends StageSceneController {
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
    private User user;

    public void setUser(User user) {this.user = user; }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void imgCloseConversationAction(){
        listener.closeChatWindow(users);
        this.stage.close();
    }

    public void drawUserList(ArrayList<User> users) {
        Platform.runLater(() -> {
            ArrayList<User> cloneList = new ArrayList<>();
            for(User u: users){
                if(!u.getUsername().equals(user.getUsername())){
                    cloneList.add(u);
                }
            }
            lvParticipants.setItems(FXCollections.observableList(cloneList));
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
        if(txtMess.getText().isEmpty()){
            return;
        }
        listener.chatText(user.getUsername(), users, txtMess.getText());
    }
    private void addChatLine(Message msg){
        if(msg.getType().equals(MessageType.CHAT_TEXT)){
            if(msg.getUserName().equals(user.getUsername())){
                setlvItemOfUser(msg.getText(), msg.getUserName());
            }
            else{
                setlvItemOfOtherUsers(msg.getText(), msg.getUserName());
            }
        }
        txtMess.clear();
    }
    private void setlvItemOfUser(String mess, String username){
        Platform.runLater(()->{HBox messItem = new HBox();

            Circle circle = new Circle();
            Image image = new Image(getClass().getClassLoader().getResource("images/avatars/" + username.toLowerCase() + ".png").toString(), 50, 50, true, true);
            circle.setRadius(15);
            circle.setFill(new ImagePattern(image));

            Label blank = new Label("  ");
            Label text = new Label(mess);
            text.setWrapText(true);
            text.setTextAlignment(TextAlignment.LEFT);

            messItem.getChildren().addAll(text, blank, circle);
            messItem.setAlignment(Pos.CENTER_RIGHT);

            lvChatLine.getItems().add(messItem);});
    }
    private void setlvItemOfOtherUsers(String mess, String username){
        Platform.runLater(()->{ HBox messItem = new HBox();

            Label blank = new Label("  ");
            Label text = new Label(mess);
            text.setWrapText(true);
            text.setTextAlignment(TextAlignment.LEFT);

            Circle circle = new Circle();
            Image image = new Image(getClass().getClassLoader().getResource("images/avatars/" + username.toLowerCase() + ".png").toString(), 50, 50, true, true);
            circle.setRadius(15);
            circle.setFill(new ImagePattern(image));

            messItem.getChildren().addAll(circle, blank, text);
            messItem.setAlignment(Pos.CENTER_LEFT);

            lvChatLine.getItems().add(messItem);});
    }
    private void soundNotification(){
         try {
             Media hit = new Media(getClass().getClassLoader().getResource("sound/sound.mp3").toString());
             MediaPlayer mediaPlayer = new MediaPlayer(hit);
             mediaPlayer.play();
         } catch (Exception e) {
             e.printStackTrace();
         }
    }


    public void onSendTextSuceeded(Message message) {
        if(!this.stage.isShowing()){
            this.stage.show();
        }
        addChatLine(message);
        soundNotification();
    }

}
