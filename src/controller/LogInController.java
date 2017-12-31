package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connection.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController extends StageSceneController implements Initializable, ConnectionCallback {
    @FXML private Label lblNoti;
    @FXML private Label lblSignUp;
    @FXML private JFXButton btnLogIn;
    @FXML private ImageView imgClose;
    @FXML private AnchorPane anchorPane;
    @FXML private JFXTextField txtusername, txthostname, txtport;
    @FXML private JFXPasswordField txtPassword;

    private double xOffset;
    private double yOffset;

    private Listener listener;

    public void closeApp() {
        Platform.exit();
        System.exit(0);
    }

    synchronized public void btnLogInAction() {
        try {
            if(txthostname.getText().isEmpty() ||txtport.getText().isEmpty()||txtusername.getText().isEmpty()||txtPassword.getText().isEmpty()){
                showNotification("Please enter full information");
                return;
            }
            String hostname = txthostname.getText();
            int port = Integer.parseInt(txtport.getText());
            String username = txtusername.getText();
            String password = txtPassword.getText();


            User user = new User(username, password);

            listener = new Listener(hostname, port, user);
            listener.setLogInController(this);
            listener.setConnectionCallback(this);

            Thread thread = new Thread(listener);
            thread.start();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void lblSignUpAction() {
        Platform.runLater(() -> {
            try {
                URL fxmlFilePath = getClass().getResource("/views/SignUp.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlFilePath);
                Parent root = loader.load();
                SignUpController controller = loader.getController();

                Stage stageSignUp = new Stage();
                controller.setStage(stageSignUp);

                stageSignUp.initStyle(StageStyle.UNDECORATED);
                stageSignUp.setScene(new Scene(root));
                stageSignUp.centerOnScreen();

                this.stage.hide();
                stageSignUp.show();
            } catch (Exception e) {
                System.err.println(e);
                lblNoti.setText("Can not load Sign Up Form");
            }
        });
    }

    public void LoadMainForm(Message msg) {
        Platform.runLater(()->{
        try {
            FXMLLoader loader = new FXMLLoader();
            URL location = getClass().getResource("/views/MainWindow.fxml");
            loader.setLocation(location);
            Parent root = loader.load();

            Stage stageMain = new Stage();
            stageMain.initStyle(StageStyle.UNDECORATED);
            stageMain.setScene(new Scene(root));

            MainWindowController controller = loader.getController();
            listener.setMainWindowController(controller);
            listener.setConnectionCallback(controller);
            controller.setListener(listener);

            User user= new User(msg.getUserName(), msg.getPass(), msg.getNickname(), msg.getStatus());
            controller.setUserMain(user);
            controller.drawUser();
            controller.setStage(stageMain);
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

    public void showNotification(String x){
        Platform.runLater(()->{
            lblNoti.setText(x);
        });
    }

    @Override
    public void onNewUserConnected(String username, String nickname, Status status) {}
    @Override
    public void onWrongInfo() {showNotification("Wrong username or password");}

    @Override
    public void onUserAlreadyLogedIn() {showNotification(txtusername.getText()+"has already logged in");}

    @Override
    public void onConnectionFailed() {
        showNotification("Wrong username or password");
    }
    @Override
    public void onSignUpFailed() {}
    @Override
    public void onUserDisconnected(String username, String nickname, Status status) {}
    @Override
    public void onConnected(Message msg) {
        LoadMainForm(msg);
    }

    @Override
    public void onChangeInfoSucceeded(Message msg) {

    }

    @Override
    public void onChangeInfoFailed(Message msg) {

    }

    @Override
    public void onReceivedMessage(Message msg) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNoti.setText("");
           /* Drag and Drop */
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
