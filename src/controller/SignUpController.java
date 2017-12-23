package controller;
import com.jfoenix.controls.JFXTextField;
import connection.ConnectionCallback;
import connection.Listener;
import connection.Message;
import connection.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;


public class SignUpController extends StageSceneController implements Initializable,ConnectionCallback{
    @FXML JFXTextField txtUsername, txtNickname, txtPassword,  txtHostname, txtPort;
    @FXML Button btnSignUp;
    @FXML private  Label lblNoti, lblLogin;
    @FXML ImageView imgClose;
    @FXML private AnchorPane anchorPane;

    private double xOffset;
    private double yOffset;


    private Listener listener;
    public void closeApp () {
        stage.close();
    }

    public void lblLogInAction(){
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
        }
        catch (Exception e)
        {
            lblNoti.setText("Can not load Log In Form");
        }
    }

    public void btnSignUpAction(){
        try{
            if (txtHostname.getText().isEmpty() || txtPort.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtNickname.getText().isEmpty()) {
                showNoti("Please enter full information");
                return;
            }
            String hostname = txtHostname.getText();
            int port = Integer.parseInt(txtPort.getText());
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String nickname = txtNickname.getText();

            User user= new User(username, password, nickname);
            listener = new Listener(hostname, port, user);
            listener.setSignUpController(this);
            listener.setConnectionCallback(this);
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public void LoadChatForm() {
        Platform.runLater(()->{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Chat.fxml"));
            Parent root = (Pane) loader.load();

            MainWindowController mainWindowController = loader.getController();
            Stage stageMain = new Stage();
            mainWindowController.setStage(stageMain);
            stageMain.setScene(new Scene(root));
            stageMain.setResizable(false);
            stageMain.initStyle(StageStyle.UNDECORATED);
            stageMain.centerOnScreen();

            this.stage.close();
            stageMain.show();

        } catch (Exception e) {
            System.err.println(e);
        }
        });
    }
    public void showNoti(String x){
        lblNoti.setText(x);
    }

    @Override
    public void onLoginSucceeded(Message message) {

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
