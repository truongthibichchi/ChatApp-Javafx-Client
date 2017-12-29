package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connection.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.convertFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SignUpController extends StageSceneController implements Initializable, ConnectionCallback {
    @FXML
    private JFXTextField txtUsername, txtNickname, txtHostname, txtPort;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private Button btnSignUp;
    @FXML
    private Label lblNoti, lblLogin;
    @FXML
    private ImageView imgClose;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Circle imgAvatar;

    private double xOffset;
    private double yOffset;

    private Listener listener;
    private ObservableList<User> users;
    private File selectedFile;
    private Image imagetoSave;

    public void closeApp() {
        stage.close();
    }

    public static void saveToFile(Image image, String username) {
        try {
            File outputFile = new File("res/images/avatars/"+username+".png");
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(bImage, "png", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch (Exception e){
            System.err.println(e);
        }
    }
    public void imgAvatarAction () {
        FileChooser fc = new FileChooser();
        fc.setTitle("Attach a file");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.png", "*.jpg"));
        selectedFile = fc.showOpenDialog(null);

        BufferedImage bufferedImage = null;
        if (selectedFile != null) {
            try {
                bufferedImage = ImageIO.read(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagetoSave = SwingFXUtils.toFXImage(bufferedImage, null);
            imgAvatar.setFill(new ImagePattern(imagetoSave));
        }
    }

    public void lblLogInAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LogIn.fxml"));
            Parent root = loader.load();
            Stage stageLogIn = new Stage();
            LogInController controller = loader.getController();
            controller.setStage(stageLogIn);

            stageLogIn.initStyle(StageStyle.UNDECORATED);
            stageLogIn.setResizable(false);
            stageLogIn.setScene(new Scene(root));
            stageLogIn.centerOnScreen();

            stageLogIn.show();
            this.stage.close();
        } catch (Exception e) {
            lblNoti.setText("Can not load Log In Form");
        }
    }

    public void btnSignUpAction() throws IOException{
        try {
            if (txtHostname.getText().isEmpty() || txtPort.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtNickname.getText().isEmpty()) {
                showNotification("Please enter full information");
                return;
            }
            String hostname = txtHostname.getText();
            int port = Integer.parseInt(txtPort.getText());
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String nickname = txtNickname.getText();


            User user = new User(username, password, nickname);
            listener = new Listener(hostname, port, user);
            listener.setSignUpController(this);
            listener.setConnectionCallback(this);
            Thread thread = new Thread(listener);
            thread.start();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void loadMainWindowForm(Message msg) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                URL location = getClass().getResource("/views/MainWindow.fxml");
                loader.setLocation(location);
                Parent root = loader.load();

                Stage stageMain = new Stage();
                stageMain.initStyle(StageStyle.UNDECORATED);
                stageMain.setScene(new Scene(root));

                MainWindowController controller = loader.getController();
                controller.setStage(stageMain);

                controller.setListener(listener);
                listener.setMainWindowController(controller);

                User user = new User(msg.getUserName(), msg.getPass(), msg.getNickname(),msg.getStatus());
                controller.setUserMain(user);
                controller.drawUser();
                listener.setConnectionCallback(controller);

                ;
                controller.addDragAndDropHandler();
                controller.setUsersData(msg.getUserListData());
                controller.drawUserList(msg.getUserListData());


                stageMain.centerOnScreen();
                stageMain.setResizable(false);

                this.stage.close();
                stageMain.show();

            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }

    public void showNotification(String x) {
        Platform.runLater(() -> {
            lblNoti.setText(x);
        });
    }

    @Override
    public void onConnected(Message msg) {
        saveToFile(imagetoSave, txtUsername.getText());
        loadMainWindowForm(msg);
    }

    @Override
    public void onWrongInfo() {}

    @Override
    public void onUserAlreadyLogedIn() {}

    @Override
    public void onConnectionFailed(){}
    @Override
    public void onSignUpFailed() {
        showNotification("This username is not available");
    }
    @Override
    public void onUserDisconnected(String username, String nickname, Status status) {}
    @Override
    public void onNewUserConnected(String username, String nickname, Status status) {}

    @Override
    public void onReCeivedAtextMessage(Message msg) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imagetoSave = new Image(getClass().getClassLoader().getResource("images/avatars/default_avatar.png").toString());
        imgAvatar.setFill(new ImagePattern(imagetoSave));

        anchorPane.setOnMousePressed(event -> {
            xOffset = this.stage.getX() - event.getScreenX();
            yOffset = this.stage.getY() - event.getScreenY();
            anchorPane.setCursor(Cursor.CLOSED_HAND);
        });

        anchorPane.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() + xOffset);
            this.stage.setY(event.getScreenY() + yOffset);

        });

        anchorPane.setOnMouseReleased(event -> {
            anchorPane.setCursor(Cursor.DEFAULT);
        });
    }
}
