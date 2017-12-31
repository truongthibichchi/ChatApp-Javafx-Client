package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connection.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    @FXML
    private ImageView imgCall;

    private double xOffset;
    private double yOffset;

    private Listener listener;
    private ArrayList<User> usersData;
    private User userMain;

    private File selectFile;
    private Image imagetoSave;
    private String username, password, nickname;
    private byte[] avatar;

    private HashMap<ArrayList<User>, ChatController> chatControllers = new HashMap<>();
    private CallController callController;


    public static void saveToFile(Image image, String username) {
        try {
            File file = new File("res/images/avatars/" + username + ".png");
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(bImage, "png", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

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
    public void setCallController(CallController controller) {
        this.callController = controller;
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

            ArrayList<User> cloneList = new ArrayList<>();
            for (User user : users) {
                if (!user.getUsername().equals(userMain.getUsername())) {
                    cloneList.add(user);
                }
            }

            lvUserList.setItems(FXCollections.observableList(cloneList));
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

    private void updateInfoForChatUsers(String username, String nickname, Status status) {
        if (chatControllers != null) {

            for (Map.Entry<ArrayList<User>, ChatController> entry : chatControllers.entrySet()) {
                ArrayList<User> users = entry.getKey();
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        user.setNickname(nickname);
                        user.setStatus(status);
                    }
                }
                ChatController controller = entry.getValue();
                controller.setUsers(users);
                controller.drawUserList(users);
            }
        }
    }

    public void imgCLoseAction() {
        Platform.exit();
        System.exit(0);
    }

    public void btnUpdateInfoAction() {
        if (txtPassword.getText().isEmpty() || txtNickname.getText().isEmpty()) {
            lblNotiUser.setText("Please enter full information");
            return;
        }
        if (selectFile == null) {
            String initFilePath = getClass().getResource("/images/avatars/" + userMain.getUsername() + ".png").getPath().replaceFirst("/", "");
            selectFile = new File(initFilePath);
            convertImageToByte();
        }
        User user = new User(txtUsername.getText(), txtPassword.getText(), txtNickname.getText(), avatar);
        listener.changeInfoUserMain(user);
    }

    public void imgAvatarAction() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Attach a file");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.png", "*.jpg"));
        selectFile = fc.showOpenDialog(null);

        BufferedImage bufferedImage = null;
        if (selectFile == null) {
            String initFilePath = getClass().getResource("/images/avatars/" + userMain.getUsername() + ".png").getPath().replaceFirst("/", "");
            selectFile = new File(initFilePath);
        }
        try {
            bufferedImage = ImageIO.read(selectFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagetoSave = SwingFXUtils.toFXImage(bufferedImage, null);
        cirAvatar.setFill(new ImagePattern(imagetoSave));
    }

    private void convertImageToByte() {
        try {
            FileInputStream fis = new FileInputStream(selectFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            for (int readNum; (readNum = fis.read(buff)) != -1; ) {
                baos.write(buff, 0, readNum);
            }
            avatar = baos.toByteArray();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void imgCallAction() {
        try {
            ObservableList<User> oberUsers = lvUserList.getSelectionModel().getSelectedItems();
            if(oberUsers.size()>1){
                lblNotiLvUser.setText("Choose only one user!");
                return;
            }
            ArrayList<User> selectedUsers = new ArrayList<>();
            selectedUsers.add(userMain);

            for (User user : oberUsers) {
                if (user.getStatus().equals(Status.BUSY)){
                    lblNotiLvUser.setText(user.getUsername()+" is busy, please try later!");
                    return;
                }
                if(user.getStatus().equals(Status.DISCONNECT)){
                    lblNotiLvUser.setText(user.getUsername()+" is not online, please try later!");
                    return;
                }
            }


            loadCallForm(userMain.getUsername(), selectedUsers);
            listener.requestCall(userMain.getUsername(), selectedUsers);

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    private void loadCallForm(String username, ArrayList<User> selectedUsers) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Call.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));

                callController = loader.getController();
                callController.setStage(stage);
                callController.addDragAndDropHandler();
                callController.setUsers(selectedUsers);
                callController.setUser(userMain);
                callController.setListener(listener);
                callController.drawUser(selectedUsers);

                stage.show();

            } catch (Exception e) {
                System.err.println(e);
            }
        });

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
                user.setStatus(Status.DISCONNECT);
            }
        }
        drawUserList(usersData);
        updateInfoForChatUsers(username, nickname, Status.DISCONNECT);
    }

    @Override
    public void onChangeInfoSucceeded(Message msg) {
        if (userMain.getUsername().equals(msg.getUserName())) {
            userMain.setNickname(msg.getNickname());
            userMain.setPass(msg.getPass());
        }
        saveToFile(imagetoSave, msg.getUserName());
        drawUser();
        drawUserList(msg.getUserListData());
    }

    @Override
    public void onChangeInfoFailed(Message msg) {
        Platform.runLater(() -> {
                    lblNotiUser.setText("Change info failed");
                }
        );
    }

    @Override
    public void onReceivedMessage(Message msg) {
        ChatController controller = null;
        for (Map.Entry<ArrayList<User>, ChatController> entry : chatControllers.entrySet()) {
            if (isEqualUsers(entry.getKey(), msg.getChatUsers())) {
                controller = entry.getValue();
                if (msg.getType().equals(MessageType.CHAT_TEXT)) {
                    controller.onSendTextSucceeded(msg);
                }
                if (msg.getType().equals(MessageType.VOICE)) {
                    controller.onSendVoiceSucceeded(msg);
                }
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

                if (msg.getType().equals(MessageType.CHAT_TEXT)) {
                    controller.onSendTextSucceeded(msg);
                }
                if (msg.getType().equals(MessageType.VOICE)) {
                    controller.onSendVoiceSucceeded(msg);
                }

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

    @Override
    public void onRequestCall(Message msg) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Call.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));

                callController = loader.getController();

                callController.setStage(stage);
                callController.addDragAndDropHandler();
                callController.setUsers(msg.getChatUsers());
                callController.setUser(userMain);
                callController.setListener(listener);
                callController.drawUser(msg.getChatUsers());
                callController.setVisibleForImgAccept(msg.getUserName());

                stage.show();

            } catch (Exception e) {
                System.err.println(e);
            }
        });

    }
}
