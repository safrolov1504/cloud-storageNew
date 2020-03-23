package com.cloud.WorkingWithMessage;


import com.cloud.Communication.Network;
import com.cloud.Controller;
import com.cloud.FileForTable;
import com.cloud.WorkingWithMessage.Message.SingIn;
import com.cloud.WorkingWithMessage.Message.WorkFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

        fileDataService.clear();

        String [] subString = str.split("<END>");
        for (String strFile:subString) {
            fileDataService.add(new FileForTable(strFile));
        }
        controller.getWorkWithTables().addInfoTableService(fileDataService);
    }

    public void workingWithInnerMessage(byte innerByte) {
        if(innerByte == CreatCommand.getCommandAuthNok() || innerByte == CreatCommand.getCommandAuthOk()){
            (new SingIn(controller,innerByte)).checkUser();
        }

        if(innerByte == CreatCommand.getSendFileOk() || innerByte == CreatCommand.getSendFileOk()){
            (new WorkFile(controller,innerByte)).addSucceed();
        }
    }

    public void getFileFromService(byte[] nameFileByte, DataInputStream inputStream) throws IOException {
        System.out.println("Get file from service");

        //get name of file
        String nameFile = new String(nameFileByte);

        //get length of file
        long lengthFile = inputStream.readLong();

        System.out.println(nameFile+" "+lengthFile);

        File file = new File("/Users/safrolov/Documents/JavaProgramming/01_readyProjects/cloud-storageNew/cloud-client/storage/"+nameFile);

        if(file.exists()){

        } else {
            //creat file
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (lengthFile>0){
                fileOutputStream.write(inputStream.readByte());
                lengthFile--;
            }
            controller.getWorkWithTables().updateTableClient();
        }


    }
}
