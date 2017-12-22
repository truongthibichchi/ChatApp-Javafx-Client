package controller;

import com.jfoenix.controls.JFXTextField;
import connection.ConnectionCallback;
import connection.Status;
import connection.User;
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
    JFXTextField txtUsername, txtPassword, txtNickname;

    @FXML
    private Button btnUpdateInfo, btnNewMess;

    @FXML
    private BorderPane borderPane;
    private double xOffset;
    private double yOffset;



    private  ObservableList<User> users;
    public void imgCloseAction() {
        Platform.exit();
        System.exit(0);
    }

    public void drawUser(User user){
        Platform.runLater(()->{
            txtUsername.setText(user.getUsername());
            txtNickname.setText(user.getNickname());
            txtPassword.setText(user.getPass());
            Image imgAvatar = new Image(getClass().getClassLoader().getResource("images/avatars/"+user.getUsername()+".png").toString());
            cirAvatar.setFill(new ImagePattern(imgAvatar));
        });

    }

    private void addChatLine(String line){

    }

    public void setUserList(){
        Platform.runLater(()->{

            ArrayList<User> list = new ArrayList<>();
            User user1 = new User("khanh", "khanh", "khanh", Status.ONLINE);
            User user2 = new User("nhi", "nhi", "nhi", Status.ONLINE);
            User user3 = new User("me", "me", "me", Status.ONLINE);
            User user4 = new User("be", "be", "be", Status.DISCONNECT);
            list.add(user1);
            list.add(user2);
            list.add(user3);
            list.add(user4);

            users = FXCollections.observableList(list);
            lvUserList.setItems(users);
            lvUserList.setCellFactory(new CellRendererInMainWindow());
            lvUserList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        });
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
    private void imgCLoseAction(){
        Platform.exit();
        System.exit(0);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
                /* Drag and Drop */
        borderPane.setOnMousePressed(event -> {
            xOffset = MainLauncher.getPrimaryStage().getX() - event.getScreenX();
            yOffset =MainLauncher.getPrimaryStage().getY() - event.getScreenY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });

        borderPane.setOnMouseDragged(event -> {
            controller.MainLauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            controller.MainLauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);

        });

        borderPane.setOnMouseReleased(event -> {
            borderPane.setCursor(Cursor.DEFAULT);
        });
    }


    @Override
    public void onConnectionFailed() {

    }
}
