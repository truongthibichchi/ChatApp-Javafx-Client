package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connection.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController extends StageSceneController implements Initializable, ConnectionCallback {
    @FXML
    private ImageView imgClose;

    @FXML Circle cirAvatar;
    @FXML
    private ListView lvUserList;
    @FXML
    JFXTextField txtUsername, txtNickname;
    @FXML private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnUpdateInfo, btnNewChat;

    @FXML
    private BorderPane borderPane;
    private double xOffset;
    private double yOffset;

    Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private  ObservableList<User> users;
    public void imgCloseAction() {
        Platform.exit();
        System.exit(0);
    }

    public void drawUser(Message msg){
        Platform.runLater(()->{
            txtUsername.setText(msg.getUserName());
            txtNickname.setText(msg.getNickname());
            Image imgAvatar = new Image(getClass().getClassLoader().getResource("images/avatars/"+msg.getUserName()+".png").toString());
            cirAvatar.setFill(new ImagePattern(imgAvatar));
        });

    }

    private void addChatLine(String line){

    }

    public void setUserList (ArrayList<User> users) {
        Platform.runLater(() -> {
            lvUserList.setItems(FXCollections.observableList(users));
            lvUserList.setCellFactory(new CellRendererInMainWindow());
            lvUserList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        });
    }

    public void setUserList(Message msg){
            users = FXCollections.observableList(msg.getUserListData());
            lvUserList.setItems(users);
            lvUserList.setCellFactory(new CellRendererInMainWindow());
            lvUserList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void btnNewMessAction() throws Exception{

        ObservableList<User> selectedUser = lvUserList.getSelectionModel().getSelectedItems();
        List<User> listOfSelectedUser = new ArrayList<>();
        for (User user : selectedUser) {
            listOfSelectedUser.add(user);
        }
        ObservableList<User> copyOfSelectedUsers = FXCollections.observableList(listOfSelectedUser);

        Platform.runLater(()-> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Chat.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));

                ChatController controller = loader.getController();
                controller.setStage(stage);
                controller.addDragAndDropHandler();
                controller.setParticipants(copyOfSelectedUsers);
                controller.drawUserList();

                stage.show();

            } catch (Exception e) {
                System.err.println(e);
            }
        });

    }

    @Override
    public void onUserDisconnected(Message msg) {
        setUserList(msg);
    }

    private void imgCLoseAction(){

        Platform.exit();
        System.exit(0);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addDragAndDropHandler() {
       /* Drag and Drop */
        borderPane.setOnMousePressed(event -> {
            xOffset = this.stage.getX() - event.getScreenX();
            yOffset =this.stage.getY() - event.getScreenY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });

        borderPane.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() + xOffset);
            this.stage.setY(event.getScreenY() + yOffset);

        });

        borderPane.setOnMouseReleased(event -> {
            borderPane.setCursor(Cursor.DEFAULT);
        });
    }


    @Override
    public void onConnected(Message msg) {

    }

    @Override
    public void onLoginFailed(Message msg) {

    }

    @Override
    public void onConnectionFailed() {

    }


    @Override
    public void onSignUpFailed(Message msg) {

    }

    @Override
    public void onNewUserConnected(Message msg) {
        setUserList(msg.getUserListData());
    }
}
