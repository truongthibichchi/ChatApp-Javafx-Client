package UI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class SignUpController extends StageSceneController{
    @FXML
    TextField txtUsername, txtNickname, txtPassword, txtEmail;

    @FXML
    Button btnSignUp;

    @FXML
    Label lblNoti, lblLogin;

    @FXML
    ImageView imgClose;

    public void closeApp () {
        Platform.exit();
        //System.exit(0);
    }

    public void lblLogInAction(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            LogInController controller = loader.getController();
            controller.setStage(stage);

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Log In");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();

            stage.show();
        }
        catch (Exception e)
        {
            lblNoti.setText("Can not load Log In Form");
        }
    }

}
