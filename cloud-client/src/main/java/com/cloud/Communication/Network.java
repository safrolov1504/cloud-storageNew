package com.cloud.Communication;

import com.cloud.Controller;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Network {
    private final String serverAddress;
    private final int port;
    private final MyClientServer myClientServer;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Scanner scannerIn;
    private Controller controller;

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public Network(String serverAddress, int port, MyClientServer myClientServer) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.myClientServer = myClientServer;

        try {
            //it's first connection or not
            initNetworkState(serverAddress, port);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Connection is failed");
            alert.setContentText("Нет подключения в серверу");
            alert.showAndWait();
        }
    }

    //creat connection
    private void initNetworkState(String serverAddress, int port) throws IOException {
        this.socket = new Socket(serverAddress,port);
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.scannerIn = new Scanner(socket.getInputStream());

        //подключили и ждем сообщений
        getMessage();
    }

    public void getMessage(){

        new Thread(() ->{
            while (true){
                //waiting for message
                try {
                    byte inByte = inputStream.readByte();
                    System.out.println(inByte);
                    Platform.runLater(() -> myClientServer.processRetrievedMessage(inByte));
                } catch (IOException e){
                    System.exit(0);
                }
            }
        }).start();
    }

    public void sendInt(int intIn) {
        try {
            outputStream.writeInt(intIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendLong(long longIn){
        try {
            outputStream.writeLong(longIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendByte(byte byteIn){
        try {
            outputStream.writeByte(byteIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(byte[] outByte) {
        try {
            outputStream.write(outByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
