package com.cloud;

import com.cloud.Communication.MyClientServer;
import com.cloud.Controllers.ChangeStage;
import com.cloud.WorkingWithMessage.GetMessage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static MyClientServer messageService;

    public static MyClientServer getMessageService() {
        return messageService;
    }
    public static void setMessageService(MyClientServer messageService) {
        App.messageService = messageService;
    }

    @Override
    public void start(Stage stage) throws IOException {
        messageService = new MyClientServer();
        ChangeStage.changeStageDo(stage, "/com/cloud/loginInterface.fxml","Welcome PC");
    }


    public static void main(String[] args) {
        launch();
    }

}