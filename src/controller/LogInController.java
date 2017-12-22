package controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController extends StageSceneController implements Initializable, ConnectionCallback {
    private static LogInController instance;
    @FXML
    private Label lblNoti;
    @FXML
    private Label lblSignUp;
    @FXML
    private JFXButton btnLogIn;
    @FXML
    private ImageView imgClose;
    private Scene scene;
    @FXML private AnchorPane anchorPane;
    private double xOffset;
    private double yOffset;

    @FXML private JFXTextField txtusername, txtpassword, txthostname, txtport;

    Listener listener;

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

    synchronized public void btnLogInAction() {
        try {
            String hostname = txthostname.getText();
            int port = Integer.parseInt(txtport.getText());
            String username = txtusername.getText();
            String password = txtpassword.getText();
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignUp.fxml"));
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
                    Stage stage = (Stage) txthostname.getScene().getWindow();
                    stage.setResizable(false);
                    stage.centerOnScreen();
                }
        );
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

            controller.drawUser(msg);
            controller.setStage(stageMain);
            controller.addDragAndDropHandler();


            stageMain.centerOnScreen();
            stageMain.setResizable(false);

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
    public void onConnectionFailed() {

    }

    @Override
    public void onLoginSucceeded(Message msg) {
            LoadMainForm(msg);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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