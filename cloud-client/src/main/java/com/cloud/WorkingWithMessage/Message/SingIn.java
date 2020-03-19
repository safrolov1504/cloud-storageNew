package com.cloud.WorkingWithMessage.Message;

import com.cloud.App;
import com.cloud.Controller;
import com.cloud.Controllers.ChangeStage;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.File;

import java.nio.file.attribute.BasicFileAttributes;

public class SingIn {
    private Controller controller;
    private byte arrayByte;
    private Alert alert = new Alert(Alert.AlertType.WARNING);
    private String userName;

    public SingIn(Controller controller, byte innerByte) {
        this.controller = controller;
        this.arrayByte = innerByte;
    }

    public void checkUser() {

        if(arrayByte == -126){
            userName = controller.textField_login.getText();
            App.setFlag(true);
            System.out.println(userName + " is it ");
            ChangeStage.changeStageDo((Stage) controller.testField_pass.getScene().getWindow(),
                    "/com/cloud/workInterface.fxml","Working window "+ controller.textField_login.getText());
//            controller.setClient(userName);


        } else if(arrayByte == -125){
            alert.setHeaderText("Authentication is failed");
            alert.setContentText("Wrong user or password");
            alert.showAndWait();
        }
    }

}
