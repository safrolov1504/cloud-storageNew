package com.cloud.WorkingWithMessage.Message;

import com.cloud.Controller;
import com.cloud.Controllers.ChangeStage;
import com.cloud.Controllers.FileForTable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
            System.out.println(userName + " is it ");
            ChangeStage.changeStageDo((Stage) controller.testField_pass.getScene().getWindow(),
                    "/com/cloud/workInterface.fxml","Working window "+ controller.textField_login.getText());
            controller.setClient(userName);

            //write files from local folder
            File folder = new File("/Users/safrolov/Documents/JavaProgramming/01_readyProjects/cloud-storageNew/cloud-client/storage");
            File[] arraFile = folder.listFiles();
            BasicFileAttributes attr;

            setColumns();

            FileForTable fileForTable;
            try {
                for (File f:arraFile) {
                    attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                    fileForTable = new FileForTable(f.getName(),attr.size()+"",attr.creationTime().toString());
                    System.out.println(f.getName()+" "+attr.size()+" "+attr.creationTime());
                    controller.fileData.add(fileForTable);
                }

                controller.table_client.setItems(controller.fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(arrayByte == -125){
            alert.setHeaderText("Authentication is failed");
            alert.setContentText("Wrong user or password");
            alert.showAndWait();
        }
    }

    private void setColumns() {
            controller.table_clientName.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("nameFileTable"));
            controller.table_clientSize.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("sizeFileTable"));
            controller.table_clientDate.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("dateCreatFileTable"));

    }
}
