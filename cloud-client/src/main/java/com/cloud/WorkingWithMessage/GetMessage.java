package com.cloud.WorkingWithMessage;


import com.cloud.Communication.Network;
import com.cloud.Controller;
import com.cloud.WorkingWithMessage.Message.SingIn;
import com.cloud.WorkingWithMessage.Message.WorkFile;

public class GetMessage {

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public GetMessage() {
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
