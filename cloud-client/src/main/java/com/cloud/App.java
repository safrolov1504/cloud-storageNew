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
    private static boolean flag;

    public static MyClientServer getMessageService() {
        return messageService;
    }


    public static boolean isFlag() {
        return flag;
    }
    public static void setFlag(boolean flag) {
        App.flag = flag;
    }

    @Override
    public void start(Stage stage) throws IOException {
        flag = false;
        messageService = new MyClientServer();
        ChangeStage.changeStageDo(stage, "/com/cloud/loginInterface.fxml","Welcome PC");
    }


    public static void main(String[] args) {
        launch();
    }

}