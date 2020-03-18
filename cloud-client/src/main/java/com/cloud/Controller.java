package com.cloud;


import com.cloud.Communication.MyClientServer;
import com.cloud.Controllers.FileForTable;
import com.cloud.WorkingWithMessage.SendMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public ObservableList<FileForTable> fileData = FXCollections.observableArrayList();

    //server
    public TableColumn table_serverName;
    public TableColumn table_serverSize;
    public TableColumn table_serverDate;



    private MyClientServer messageService;
    private SendMessage sendMessage;

    private String client;

    public void setClient(String client) {
        this.client = client;
    }

    public void shutdown() {
        //System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table_client = new TableView<>();
        table_clientName = new TableColumn<>();
        table_clientSize = new TableColumn<>();
        table_clientDate = new TableColumn<>();
        try{
            this.messageService = new MyClientServer(this);
            this.sendMessage = new SendMessage(this.messageService);
        } catch (Exception e) {
            e.printStackTrace();
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
        File file = new File("cloud-client/storage/1.txt");

        try {
            sendMessage.sendFileToServer(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void button_delete(ActionEvent actionEvent) {
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
    }
}
