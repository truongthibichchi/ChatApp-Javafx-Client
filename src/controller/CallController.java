package controller;

import connection.Listener;
import connection.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import util.CallRecorder;
import util.CallUtil;
import util.VoicePlayback;
import util.VoiceUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CallController extends StageSceneController implements Initializable {
    @FXML private BorderPane pane;
    @FXML private ImageView imgClose;
    @FXML private Circle cirAvatar;
    @FXML private Label lblNickname, lblNoti;
    @FXML private ImageView imgAccept, imgDecline;

    private double xOffset;
    private double yOffset;

    private ArrayList<User> users;

    private Listener listener;
    private User userMain;



    MediaPlayer mediaPlayer= null;

    public void setUser(User user) {
        this.userMain = user;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void imgCloseAction(){
        mediaPlayer.stop();
        try {
            listener.respondCallDecline(userMain.getUsername(), users);
        } catch (IOException e) {
            System.err.println(e);
        }
        declineCall();
        this.stage.close();
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

    public void drawUser(ArrayList<User> users){
        Platform.runLater(() -> {
           for(User user: users){
               if(!user.getUsername().equals(userMain.getUsername())){
                   Image imgAvatar = new Image(getClass().getClassLoader().getResource("images/avatars/" + user.getUsername() + ".png").toString());
                   cirAvatar.setFill(new ImagePattern(imgAvatar));
                   lblNickname.setText(user.getUsername());
               }
           }
        });
    }

    public void setVisibleForImgAccept(String username){
        if (!userMain.getUsername().equals(username)){
            imgAccept.setVisible(true);
        }
    }

    public void showNoti(String x){
        Platform.runLater(()->{
            lblNoti.setText(x);
        });
    }

    public void imgAcceptAction() throws IOException {
        mediaPlayer.stop();
        imgAccept.setVisible(false);
        showNoti("connected!");
        listener.respondCallAccept(userMain.getUsername(), users);
        captureAudio();
    }

    public void captureAudio(){
        CallRecorder recorder = new CallRecorder();
        recorder.setUsername(userMain.getUsername());
        recorder.setParticipants(users);
        recorder.captureAudio();
        imgAccept.setVisible(false);
    }


    public void imgDeclineAction() throws IOException {
        listener.respondCallDecline(userMain.getUsername(), users);
        declineCall();
    }

    public void declineCall(){
        mediaPlayer.stop();
        CallUtil.setCalling(false);
        showNoti("End call!");
        imgAccept.setVisible(false);
    }

    public void playback(byte[] audio){
        mediaPlayer.stop();
        VoicePlayback voicePlayback = new VoicePlayback();
        voicePlayback.playAudio(audio);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgAccept.setVisible(false);
    }
}
