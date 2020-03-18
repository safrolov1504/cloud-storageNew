package com.cloud.WorkingWithMessage.Message;


import com.cloud.Controller;
import javafx.scene.control.Alert;


public class WorkFile {
    private Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    Controller controller;

    public WorkFile(Controller controller) {
        this.controller = controller;
    }

    public void addSucceed(){
        alert.setHeaderText("File");
        alert.setContentText("file was send");
        alert.showAndWait();
    }
}
