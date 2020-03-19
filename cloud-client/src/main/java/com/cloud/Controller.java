package com.cloud;


import com.cloud.Communication.MyClientServer;
import com.cloud.WorkingWithMessage.SendMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
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
    public ObservableList<FileForTable> fileDataClient = FXCollections.observableArrayList();

    //server
    public TableView<FileForTable> table_service;
    public TableColumn<FileForTable,String> table_serverName;
    public TableColumn<FileForTable,String> table_serverSize;
    public TableColumn<FileForTable,String> table_serverDate;
    public ObservableList<FileForTable> fileDataService = FXCollections.observableArrayList();

    private MyClientServer messageService;
    private SendMessage sendMessage;

    public void shutdown() {
        //System.exit(0);
    }

    public void setFileDataService(ObservableList<FileForTable> fileDataService) {
        this.fileDataService = fileDataService;
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
            sendMessage.sendRequestToGetListFileFromService();
            creatTable();
        }
    }

    private void creatTable() {
        this.table_clientName.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("nameFileTable"));
        this.table_clientSize.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("sizeFileTable"));
        this.table_clientDate.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("dateCreatFileTable"));

        this.table_serverName.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("nameFileTable"));
        this.table_serverSize.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("sizeFileTable"));
        this.table_serverDate.setCellValueFactory(new PropertyValueFactory<FileForTable,String>("dateCreatFileTable"));

        //write files from local folder
        File folder = new File("/Users/safrolov/Documents/JavaProgramming/01_readyProjects/cloud-storageNew/cloud-client/storage");
        File[] arrayFile = folder.listFiles();
        BasicFileAttributes attr;

        FileForTable fileForTable;
        try {
            for (File f:arrayFile) {
                attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                fileForTable = new FileForTable(f.getName(),attr.size()+"",attr.creationTime().toString());
                System.out.println(f.getName()+" "+attr.size()+" "+attr.creationTime());
                this.fileDataClient.add(fileForTable);
            }

            this.table_client.setItems(this.fileDataClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.table_service.setItems(this.fileDataService);
    }

    //button login
    @FXML
    public void login_buttonSignIn(ActionEvent actionEvent) {
        sendMessage.sendSighIn(textField_login.getText(), testField_pass.getText());
    }

    //button clients
    @FXML
    public void button_sendToService(ActionEvent actionEvent) {
        File file = new File("cloud-client/storage/unnamed.jpg");

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
