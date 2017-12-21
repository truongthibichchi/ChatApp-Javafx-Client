package UI;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class SignUpController extends StageSceneController{
    @FXML
    JFXTextField txtUsername, txtNickname, txtPassword, txtEmail, txtHostname, txtPort;

    @FXML
    Button btnSignUp;

    @FXML
    private  Label lblNoti, lblLogin;

    @FXML
    ImageView imgClose;
    private  Listener listener;
    public void closeApp () {
        stage.close();
    }

    public void lblLogInAction(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../res/views/LogIn.fxml"));
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
            String hostname = txtHostname.getText();
            int port = Integer.parseInt(txtPort.getText());
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String nickname = txtNickname.getText();
            User user= new User(username, password, nickname);
            listener = new Listener(hostname, port, user);
            listener.signUpController=this;
            listener.SignUp(user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void LoadChatForm() {
        Platform.runLater(()->{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Chat.fxml"));
            Parent root = (Pane) loader.load();

            ChatController chatController = loader.getController();
            chatController.setListener(listener);

            Stage stageChat = new Stage();
            chatController.setStage(stageChat);
            stageChat.setScene(new Scene(root));
            stageChat.setResizable(false);
            stageChat.initStyle(StageStyle.UNDECORATED);
            stageChat.centerOnScreen();

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
