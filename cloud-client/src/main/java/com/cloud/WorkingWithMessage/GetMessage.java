package com.cloud.WorkingWithMessage;


import com.cloud.Communication.Network;
import com.cloud.Controller;
import com.cloud.FileForTable;
import com.cloud.WorkingWithMessage.Message.SingIn;
import com.cloud.WorkingWithMessage.Message.WorkFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class GetMessage {

    private Controller controller;
    public ObservableList<FileForTable> fileDataService = FXCollections.observableArrayList();

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public GetMessage() {
    }

    public void getListFile(byte[] bytesIn){
        System.out.println(Arrays.toString(bytesIn));
        String str = new String(bytesIn);
        System.out.println(str);

        String [] subString = str.split("<END>");
        for (String strFile:subString) {
            //System.out.println(strFile);
            //System.out.println(new FileForTable(strFile));
            fileDataService.add(new FileForTable(strFile));
        }
        controller.setFileDataService(fileDataService);
    }

    public void workingWithInnerMessage(byte innerByte) {
        if(innerByte == CreatCommand.getCommandAuthNok() || innerByte == CreatCommand.getCommandAuthOk()){
            (new SingIn(controller,innerByte)).checkUser();
        }
        if(innerByte == CreatCommand.getSendFileOk() || innerByte == CreatCommand.getSendFileOk()){
            (new WorkFile(controller,innerByte)).addSucceed();
        }
        }

}
