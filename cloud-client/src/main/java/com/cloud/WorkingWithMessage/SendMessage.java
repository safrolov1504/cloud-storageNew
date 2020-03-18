package com.cloud.WorkingWithMessage;


import com.cloud.Communication.MyClientServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


public class SendMessage {
    private MyClientServer messageService;

    public SendMessage(MyClientServer messageService) {
        this.messageService = messageService;
    }

    public void sendSighIn(String login, String pass) {
        messageService.sendByte(CreatCommand.getCommandAuth());

        messageService.sendInt(login.length());
        messageService.sendMessage(login.getBytes());
        messageService.sendInt(pass.length());
        messageService.sendMessage(pass.getBytes());
    }


    public void sendFileToServer(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file.getPath());
        long lengthFile = file.length();
        byte[] byteArray;
        int i;

        messageService.sendByte(CreatCommand.getSendFile());
//        messageService.sendInt(file.getName().length());
//        messageService.sendMessage(file.getName().getBytes());
//
//
//        messageService.sendLong(lengthFile);
//
//        //начинаем работу с файлом
//
//        boolean flag=true;

//        while (flag){
//                if(lengthFile<1024){
//                    byteArray = new byte[(int) lengthFile];
//                } else {
//                    byteArray = new byte[1024];
//                }
//                i = fileInputStream.read(byteArray);
//                lengthFile-=i;
//                messageService.sendMessage(byteArray);
//                System.out.println(i+" "+lengthFile);
//                System.out.println(Arrays.toString(byteArray));
//
//        }
//
//        fileInputStream.close();
    }
}
