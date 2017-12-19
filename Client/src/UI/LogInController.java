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


//import java.awt.*;


public class LogInController extends StageSceneController {
    @FXML
    private TextField txtUsername, txtPassword, txtPort, txtHostname;

    @FXML
    private Label lblSignUp, lblNoti;

    @FXML
    private Button btnLogIn;

    @FXML
    private ImageView imgClose;

    private  Listener listener;
    public static ChatWindowController chatController;
    private Scene scene;
    public void closeApp() {
        Platform.exit();
        System.exit(0);
    }

    private static LogInController instance;
    public LogInController(){instance=this;}
    public  static LogInController getInstance(){return instance; }


    public void lblSignUpAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            SignUpController controller = loader.getController();
            controller.setStage(stage);

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Log In");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();

            stage.show();
        } catch (Exception e) {
            lblNoti.setText("Can not load Sign Up Form");
        }
    }

    public void btnLogInAction() {
        try {
            String hostname = txtHostname.getText();
            int port = Integer.parseInt(txtPort.getText());
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            User user= new User(username, password);
            Listener listener= new Listener(hostname, port, user);
            Thread thread= new Thread(listener);
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void showErrorDialog (String message){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(message);
            alert.setContentText("Please check for firewall issues and check if the server is running.");
            alert.showAndWait();
    }
    public void showScene() throws IOException{
        Platform.runLater(()-> {
                    Stage stage = (Stage) txtHostname.getScene().getWindow();
                    stage.setResizable(false);
                   stage.centerOnScreen();
                }
        );
    }

    public void LoadChatForm(String username, String pass){
        try{

            User user = new User(username, pass);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatWindow.fxml"));
            Parent root = loader.load();
            chatController =loader.getController();
            chatController.setUser(user);

            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
            // this.scene= new Scene(root);
        }catch (Exception e){
            lblNoti.setText("Can not load Chat Window Form");
        }
    }
}
