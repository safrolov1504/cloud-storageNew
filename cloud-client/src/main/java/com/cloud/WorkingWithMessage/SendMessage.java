package com.cloud.WorkingWithMessage;


import com.cloud.Communication.MyClientServer;
import com.cloud.Communication.Network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


public class SendMessage {
    private Network network;

    public SendMessage(Network network) {
        this.network = network;
    }

    public void sendSighIn(String login, String pass) {
        network.sendByte(CreatCommand.getCommandAuth());

        network.sendInt(login.length());
        network.sendMessage(login.getBytes());
        network.sendInt(pass.length());
        network.sendMessage(pass.getBytes());
    }


    public void sendFileToServer(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file.getPath());
        long lengthFile = file.length();
        byte[] byteArray;
        int i;

        network.sendByte(CreatCommand.getSendFile());
        network.sendInt(file.getName().length());
        network.sendMessage(file.getName().getBytes());


        network.sendLong(lengthFile);

        //начинаем работу с файлом

        boolean flag=true;

        while (flag){
                if(lengthFile<1024){
                    byteArray = new byte[(int) lengthFile];
                    flag = false;
                } else {
                    byteArray = new byte[1024];
                }
                i = fileInputStream.read(byteArray);
                lengthFile-=i;
                network.sendMessage(byteArray);
                System.out.println(i+" "+lengthFile);
                System.out.println(Arrays.toString(byteArray));
        }

        fileInputStream.close();
    }
}
