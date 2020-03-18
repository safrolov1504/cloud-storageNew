package com.cloud.WorkingWithMessage.Message;


import com.cloud.Controller;
import com.cloud.WorkingWithMessage.CreatCommand;
import javafx.scene.control.Alert;


public class WorkFile {
    private Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    private Controller controller;
    private byte inner;

    public WorkFile(Controller controller, byte inner) {
        this.controller = controller;
        this.inner = inner;
    }

    public void addSucceed(){
        if(inner == CreatCommand.getSendFileOk()){
            alert.setHeaderText("File");
            alert.setContentText("file was send");
            alert.showAndWait();
        }
    }
}
