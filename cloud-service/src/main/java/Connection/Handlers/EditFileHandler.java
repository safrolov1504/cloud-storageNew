package Connection.Handlers;

import Connection.CreatCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import workingWithMessage.SendFileToClient;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;


// Идет после FirstHandler в конвеере
public class EditFileHandler extends ChannelInboundHandlerAdapter {
//    private AddFile addFile;
    public enum StateSecond{
        IDLE, SEND_FILE_TO_CLIENT
    }

    private StateSecond stateSecond = StateSecond.IDLE;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String [] aboutFile = (String[]) msg;
        System.out.println(Arrays.toString(aboutFile));

        if(aboutFile[0].equals(StateSecond.SEND_FILE_TO_CLIENT.toString())){
            stateSecond = StateSecond.SEND_FILE_TO_CLIENT;
        }

        String nameFile = aboutFile[1];
        String userName = aboutFile[2];

        //open file
        File file = new File("/Users/safrolov/Documents/JavaProgramming/01_readyProjects/cloud-storageNew/cloud-service/global-storage/"+userName+"/"+nameFile);
        FileInputStream fileInputStream = new FileInputStream(file.getPath());

        //if it needs to send file to client
        if(stateSecond == StateSecond.SEND_FILE_TO_CLIENT) {
            //if file is not exist, then send error command
            if (!file.exists()) {
                sendBack(ctx, CreatCommand.getGetFileNOk());
                return;
            }

            //int lengthNameFile = nameFile.length();
            long lengthFile = file.length();

            //send command CreatCommand.getGetFile()
            sendBack(ctx, CreatCommand.getGetFileOk());
            //send length of name
            sendBackBytes(ctx,SendFileToClient.getLengthName(nameFile));
            //send name
            sendBackBytes(ctx, SendFileToClient.getByteName(nameFile));
            //send length of file
            sendBackBytes(ctx,SendFileToClient.longToBytes(lengthFile));
            //send file
            SendFileToClient.sendFile(ctx, lengthFile, fileInputStream);
            //ctx.writeAndFlush(fileInputStream.read());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void sendBack(ChannelHandlerContext ctx, byte out){
        ByteBuf buf = ctx.alloc().buffer(2);
        buf.writeByte(out);
        ctx.writeAndFlush(buf);
    }

    public void sendBackBytes(ChannelHandlerContext ctx, byte [] arr){
        ByteBuf buf = ctx.alloc().buffer(arr.length);
        System.out.println(Arrays.toString(arr));
        buf.writeBytes(arr);
        ctx.writeAndFlush(buf);
    }
}
