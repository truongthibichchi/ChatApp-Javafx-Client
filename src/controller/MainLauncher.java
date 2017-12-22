package controller;

import connection.Listener;
import connection.User;
import controller.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainLauncher extends Application {

    private static Stage primaryStageObj;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStageObj = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainWindow.fxml"));
        Parent root = loader.load();

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        MainWindowController controller = loader.getController();
        controller.setStage(primaryStage);
        User user = new User("chi", "chi", "chi");

        Listener listener = new Listener("localhost", 9999, user);
        listener.setMainWindowController(controller);
        listener.setConnectionCallback(controller);

        Thread thread = new Thread(listener);
        thread.start();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }
}
