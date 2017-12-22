package controller;

import javafx.stage.Stage;

public class StageSceneController {
    protected Stage stage;
    protected StageSceneController controllerCha;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setStatus(String status) {}
    public void setControllerCha(StageSceneController controllerCha) {
        this.controllerCha = controllerCha;
    }
    public void capNhatDuLieu(Object o, boolean flag){}
}
