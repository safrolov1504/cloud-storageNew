package com.cloud.Communication;

import com.cloud.WorkingWithMessage.CreatCommand;
import com.cloud.WorkingWithMessage.GetMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class MyClientServer {
    //for communication with server
    private static final String HOST_ADDRESS_PROP = "server.address";
    private static final String HOST_PORT_PROP = "server.port";
    private String hostAddress;
    private int hostPort;
    private GetMessage getMessage;
    private Network network;

    public Network getNetwork() {
        return network;
    }

    public GetMessage getGetMessage() {
        return getMessage;
    }

    public MyClientServer() {
        getMessage = new GetMessage();
        initialise();
    }

    private void initialise() {
        readProperties();
        startConnectionToServer();
    }

    //read properties for connection
    private void readProperties() {
        Properties serverProperty = new Properties();
        try(InputStream inputStream = getClass().getResourceAsStream("/com/cloud/application.properties")){
            serverProperty.load(inputStream);
            hostAddress = serverProperty.getProperty(HOST_ADDRESS_PROP);
            hostPort = Integer.parseInt(serverProperty.getProperty(HOST_PORT_PROP));
        } catch (IOException e) {
            new RuntimeException("Failed to read application.properties file", e);
        }
    }

    private void startConnectionToServer() {
        this.network = new Network(hostAddress, hostPort, this);
    }


    public void processRetrievedMessage(byte innerByte) {
        getMessage.workingWithInnerMessage(innerByte);
    }

    public void getListFile(ArrayList<Byte> bytes) {
        bytes.remove(0);
        bytes.remove(bytes.size()-1);
        byte [] bytes1 = new byte[bytes.size()];
        int i = 0;
        for (byte b:bytes) {
            bytes1[i] = b;
            i++;
        }
        getMessage.getListFile(bytes1);
    }
}
