package com.cloud;


import com.cloud.Communication.MyClientServer;
import com.cloud.Controllers.WorkWithTables;
import com.cloud.WorkingWithMessage.SendMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //Login window
    public TextField textField_login;
    public PasswordField testField_pass;


    //work window
    //client
    public ProgressBar pb_client;
    public TableView<FileForTable> table_client;
    public TableColumn<FileForTable,String> table_clientName;
    public TableColumn<FileForTable,String> table_clientSize;
    public TableColumn<FileForTable,String> table_clientDate;
//    public ObservableList<FileForTable> fileDataClient = FXCollections.observableArrayList();


    //server
    public TableView<FileForTable> table_service;
    public TableColumn<FileForTable,String> table_serverName;
    public TableColumn<FileForTable,String> table_serverSize;
    public TableColumn<FileForTable,String> table_serverDate;
//    public ObservableList<FileForTable> fileDataService = FXCollections.observableArrayList();

    private MyClientServer messageService;
    private SendMessage sendMessage;
    private WorkWithTables workWithTables;

    public void shutdown() {
        //System.exit(0);
    }

    public WorkWithTables getWorkWithTables() {
        return workWithTables;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            this.messageService = App.getMessageService();
            this.sendMessage = new SendMessage(this.messageService.getNetwork());
            App.getMessageService().getGetMessage().setController(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(App.isFlag()){
            //creatTables();
            workWithTables = new WorkWithTables(this,sendMessage);
            workWithTables.updateTableClient();

            workWithTables.updateTableService();
        }
    }

    //button login
    @FXML
    public void login_buttonSignIn(ActionEvent actionEvent) {
        sendMessage.sendSighIn(textField_login.getText(), testField_pass.getText());
    }

    //button clients
    @FXML
    public void button_sendToService(ActionEvent actionEvent) {
        File file;

        FileForTable selectedFile = table_client.getSelectionModel().getSelectedItem();
        System.out.println(selectedFile);
        if(selectedFile !=null){
            file = new File("cloud-client/storage/"+selectedFile.nameFileTable);
            try {
                sendMessage.sendFileToServer(file);
                workWithTables.updateTableService();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectedFile=null;
        }
    }

    @FXML
    public void button_delete(ActionEvent actionEvent) {
        File file;

        FileForTable selectedFile = table_client.getSelectionModel().getSelectedItem();
        System.out.println(selectedFile);
        if(selectedFile !=null) {
            file = new File("cloud-client/storage/" + selectedFile.nameFileTable);
            file.delete();
            selectedFile = null;
        }
        workWithTables.updateTableClient();
    }

    @FXML
    public void button_edit(ActionEvent actionEvent) {
    }

    //button server
    @FXML
    public void button_sendToClient(ActionEvent actionEvent) {
    }

    @FXML
    public void button_exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
