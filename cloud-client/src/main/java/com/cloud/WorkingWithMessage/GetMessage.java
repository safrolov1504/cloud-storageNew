package com.cloud.WorkingWithMessage;


import com.cloud.Communication.Network;
import com.cloud.Controller;
import com.cloud.WorkingWithMessage.Message.SingIn;

public class GetMessage {

    private Network network;
    private Controller controller;

    public GetMessage(Network network, Controller controller) {
        this.controller = controller;
        this.network = network;
    }

    public void workingWithInnerMessage(byte innerByte) {
        if(innerByte == CreatCommand.getCommandAuthNok() || innerByte == CreatCommand.getCommandAuthOk()){
            (new SingIn(controller,innerByte)).checkUser();
        }
        }
}
