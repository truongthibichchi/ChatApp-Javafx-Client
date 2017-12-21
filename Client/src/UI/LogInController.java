package UI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class LogInController extends StageSceneController {
    private static LogInController instance;
    @FXML
    private Label lblNoti;
    @FXML
    private TextField txtUsername, txtPassword, txtPort, txtHostname;
    @FXML
    private Label lblSignUp;
    @FXML
    private Button btnLogIn;
    @FXML
    private ImageView imgClose;
    private Listener listener;
    private Scene scene;

    public LogInController() {
        instance = this;
    }

    public static LogInController getInstance() {
        return instance;
    }

    public void closeApp() {
        Platform.exit();
        System.exit(0);
    }

    public void btnLogInAction() {
        try {
            String hostname = txtHostname.getText();
            int port = Integer.parseInt(txtPort.getText());
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            User user = new User(username, password);
            listener = new Listener(hostname, port, user);
            listener.logIncontroller = this;
            listener.LogIn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lblSignUpAction() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignUp.fxml"));
                Parent root = loader.load();
                SignUpController controller = loader.getController();

                Stage stageSignUp = new Stage();
                controller.setStage(stageSignUp);
                stageSignUp.initStyle(StageStyle.UNDECORATED);
                stageSignUp.setTitle("Log In");
                stageSignUp.setResizable(false);
                stageSignUp.setScene(new Scene(root));
                stageSignUp.centerOnScreen();
                this.stage.hide();
                stageSignUp.show();
            } catch (Exception e) {
                lblNoti.setText("Can not load Sign Up Form");
            }
        });
    }


    public void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(message);
        alert.setContentText("Please check for firewall issues and check if the server is running.");
        alert.showAndWait();
    }

    public void showScene() throws IOException {
        Platform.runLater(() -> {
                    Stage stage = (Stage) txtHostname.getScene().getWindow();
                    stage.setResizable(false);
                    stage.centerOnScreen();
                }
        );
    }

    public void LoadChatForm() {
        Platform.runLater(()->{
        try {
            FXMLLoader loader = new FXMLLoader();
            URL location = getClass().getResource("/views/Chat.fxml");
            loader.setLocation(location);
            Parent root = loader.load();

            ChatController controller = loader.getController();
            controller.setListener(listener);
            Stage stageChat = new Stage();
            controller.setStage(stageChat);
            stageChat.setScene(new Scene(root));
            stageChat.initStyle(StageStyle.UNDECORATED);
            stageChat.centerOnScreen();
            stageChat.setResizable(false);

            this.stage.close();
            stageChat.show();
        } catch (Exception e) {
            System.err.println(e);
        }
        });
    }
    public void showNoti(String x){
        lblNoti.setText(x);
    }
}
