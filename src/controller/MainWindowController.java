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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindowController extends StageSceneController implements Initializable, ConnectionCallback {
    @FXML
    private ImageView imgClose;
    @FXML
    private Circle cirAvatar;
    @FXML
    private ListView lvUserList;
    @FXML
    private JFXTextField txtUsername, txtNickname;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXButton btnUpdateInfo, btnNewChat;
    @FXML
    private Label lblNotiUser, lblNotiLvUser;
    @FXML
    private BorderPane borderPane;

    private double xOffset;
    private double yOffset;

    private Listener listener;
    private ArrayList<User> usersData;
    private User userMain;

    private HashMap<ArrayList<User>, ChatController> chatControllers = new HashMap<>();

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setUsersData(ArrayList<User> usersData) {
        this.usersData = usersData;
    }

    public void setUserMain(User user) {
        this.userMain = user;
    }

    public void setChatControllers(ArrayList<User> users, ChatController controller) {
        chatControllers.put(users, controller);
    }

    public void imgCloseAction() {
        Platform.exit();
        System.exit(0);
    }

    public void drawUser() {
        Platform.runLater(() -> {
            txtUsername.setText(userMain.getUsername());
            txtNickname.setText(userMain.getNickname());
            Image imgAvatar = new Image(getClass().getClassLoader().getResource("images/avatars/" + userMain.getUsername() + ".png").toString());
            cirAvatar.setFill(new ImagePattern(imgAvatar));
        });
    }

    public void drawUserList(ArrayList<User> users) {
        Platform.runLater(() -> {
            lvUserList.setItems(FXCollections.observableList(users));
            lvUserList.setCellFactory(new CellRendererInMainWindow());
            lvUserList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        });
    }

    public void btnNewMessAction() throws Exception {
        try {
            ObservableList<User> oberUsers = lvUserList.getSelectionModel().getSelectedItems();

            ArrayList<User> selectedUsers = new ArrayList<>();
            for (User user : oberUsers) {
                selectedUsers.add(user);
            }

            selectedUsers.add(userMain);
            if (isGroupChatExisted(selectedUsers)) {
                lblNotiLvUser.setText("Group chat existed");
                return;
            } else {
                lblNotiLvUser.setText("");
            }
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Chat.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root));

                    ChatController controller = loader.getController();

                    controller.setStage(stage);
                    controller.addDragAndDropHandler();
                    controller.setUsers(selectedUsers);
                    controller.setUser(userMain);
                    controller.setListener(listener);
                    controller.drawUserList(selectedUsers);

                    setChatControllers(selectedUsers, controller);

                    stage.show();

                } catch (Exception e) {
                    System.err.println(e);
                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private boolean isGroupChatExisted(ArrayList<User> users) {
        if (chatControllers.containsKey(users)) {
            return true;
        }
        return false;
    }

    private void updateInfoForChatUsers(String username, String nickname, Status status ){
        for(Map.Entry<ArrayList<User>, ChatController > entry: chatControllers.entrySet()){
            ArrayList< User> users = entry.getKey();
            for(User user:users){
                if(user.getUsername().equals(username)){
                    user.setNickname(nickname);
                    user.setStatus(status);
                }
            }
            ChatController controller = entry.getValue();
            controller.setUsers(users);
            controller.drawUserList(users);
        }
    }

    public void imgCLoseAction() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUsername.setEditable(false);
        Image imgAvatar = new Image(getClass().getClassLoader().getResource("images/avatars/default_avatar.png").toString());
        cirAvatar.setFill(new ImagePattern(imgAvatar));
    }

    public void addDragAndDropHandler() {
       /* Drag and Drop */
        borderPane.setOnMousePressed(event -> {
            xOffset = this.stage.getX() - event.getScreenX();
            yOffset = this.stage.getY() - event.getScreenY();
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
    public void onWrongInfo() {
    }

    @Override
    public void onUserAlreadyLogedIn() {
    }

    @Override
    public void onConnectionFailed() {
    }

    @Override
    public void onSignUpFailed() {

    }

    @Override
    public void onNewUserConnected(String username, String nickname, Status status) {
        int count = 0;
        for (User user : usersData) {
            if (user.getUsername().equals(username)) {
                count++;
                user.setStatus(status);
                break;
            }
        }
        if (count == 0) {
            //sign up
            usersData.add(new User(username, nickname, status));
        }
        drawUserList(usersData);
        updateInfoForChatUsers(username, nickname, status);
    }

    @Override
    public void onUserDisconnected(String username, String nickname, Status status) {
        for (User user : usersData) {
            if (user.getUsername().equals(username)) {
                user.setStatus(status);
            }
        }
        drawUserList(usersData);
        updateInfoForChatUsers(username, nickname, status);
    }

    @Override
    public void onReCeivedAtextMessage(Message msg) {
        ChatController controller = null;
        for (Map.Entry<ArrayList<User>, ChatController> entry : chatControllers.entrySet()) {
            if (isEqualUsers(entry.getKey(), msg.getChatUsers())) {
                controller = entry.getValue();
                controller.onSendTextSuceeded(msg);
                return;
            }
        }
        loadNewChatWindow(msg);
    }

    private boolean isEqualUsers(ArrayList<User> msgUsers, ArrayList<User> mapUsers) {
        String msgUsername = "";
        String mapUsename = "";
        if (msgUsers.size() != mapUsers.size()) {
            return false;
        }
        for (User user : msgUsers) {
            msgUsername += user.getUsername();
        }

        for (User user : mapUsers) {
            mapUsename += user.getUsername();
        }
        if (!msgUsername.equals(mapUsename)) {
            return false;
        }
        return true;
    }

    private void loadNewChatWindow(Message msg) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Chat.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));

                ChatController controller = loader.getController();

                controller.setStage(stage);
                controller.addDragAndDropHandler();
                controller.setUsers(msg.getChatUsers());
                controller.setUser(userMain);
                controller.setListener(listener);
                controller.drawUserList(msg.getChatUsers());

                setChatControllers(msg.getChatUsers(), controller);
                updateInfoForChatUsers(userMain.getUsername(), userMain.getNickname(), userMain.getStatus());
                controller.onSendTextSuceeded(msg);

                stage.show();

            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }

    public void closeChatWindow(ArrayList<User> users) {
        for (Map.Entry<ArrayList<User>, ChatController> entry : chatControllers.entrySet()) {
            if (isEqualUsers(entry.getKey(), users)) {
                chatControllers.remove(users);
            }
        }

    }
}
