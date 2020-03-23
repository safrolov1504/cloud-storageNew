package Connection.Handlers;

import Connection.CreatCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import workingWithMessage.ListFilesServer;

import java.io.File;
import java.io.FileOutputStream;


// Идет после FirstHandler в конвеере
public class FileHandler extends ChannelInboundHandlerAdapter {
    public enum State {
        IDLE, NAME_LENGTH, NAME, FILE_LENGTH, FILE, GET
    }

    public enum StateSecond{
        IDLE, NEXT_HANDLER
    }

    private State state = State.IDLE;
    private StateSecond stateSecond = StateSecond.IDLE;

    private int fileNameLength;
    private String fileName;
    private long fileLength;
    private long receivedFileLength;

    private File file;
    private FileOutputStream fileOutputStream;
    private String userName;

    public FileHandler(String userName) {
        this.userName = userName;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        String [] strings = new String[3];
        while (buf.readableBytes()>0){
            if(state == State.IDLE){
                byte read = buf.readByte();
                if(read == CreatCommand.getSendFile()){
                    state = State.NAME_LENGTH;
                } else if(read == CreatCommand.getSendListFileFromService()) {
                    //System.out.println("get command to send list "+ CreatCommand.getSendListFileFromService());
                    sendList(ctx);
                    //break;
                } else if(read == CreatCommand.getGetFile()){
                    state = State.NAME_LENGTH;
                    stateSecond = StateSecond.NEXT_HANDLER;
                    strings[0] = "SEND_FILE_TO_CLIENT";
                } else {
                    System.out.println("ERROR: Invalid first byte - " + read);
                    //сделать переход в статус перекидования в следующий handler
                    //state = State.NEXT_HANDLER;
                }
            }

            if(state == State.NAME_LENGTH){
                if(buf.readableBytes() >= 4){
                    fileNameLength = buf.readInt();
                    state = State.NAME;
                }
            }

            if(state == State.NAME){
                if(buf.readableBytes()>= fileNameLength){
                    byte[] tmpBuf = new byte[fileNameLength];
                    buf.readBytes(tmpBuf);
                    fileName = new String(tmpBuf,"UTF-8");
                    state = State.FILE_LENGTH;
                }
            }

            if(stateSecond == StateSecond.NEXT_HANDLER){
                strings[1] = fileName;
                strings[2] = userName;
                ctx.fireChannelRead(strings);
                state = State.IDLE;
                stateSecond = StateSecond.IDLE;
            }

            if(state == State.FILE_LENGTH){
                if(buf.readableBytes()>= 8){
                    fileLength = buf.readLong();

                    //getting ready for taking the file
                    //creat folders if it needs
                    file = new File("/Users/safrolov/Documents/JavaProgramming/01_readyProjects/cloud-storageNew/cloud-service/global-storage/"+userName);
                    if(!file.exists()){
                        file.mkdirs();
                    }

                    //creat file
                    file = new File(file.getPath()+"/"+ fileName);
                    file.createNewFile();
                    fileOutputStream = new FileOutputStream(file);
                    state = State.FILE;
                }
            }

            if(state == State.FILE){
                while (buf.readableBytes()>0){
                    fileOutputStream.write(buf.readByte());
                    receivedFileLength++;
                    if(receivedFileLength == fileLength){
                        state = State.IDLE;
                        fileOutputStream.close();
                        System.out.println(userName+" "+ fileName +" "+ fileLength);
                        sendBack(ctx, CreatCommand.getSendFileOk());
                        break;
                    }
                }
                //break;
            }
        }
    }

    private void sendList(ChannelHandlerContext ctx){
        sendBack(ctx,CreatCommand.getSendListFileFromService());
        sendBackBytes(ctx,ListFilesServer.creatFileList(userName));
        //sendBack(ctx,CreatCommand.getSendListFileFromServiceEnd());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void sendBackBytes(ChannelHandlerContext ctx, byte [] arr){
        ByteBuf buf = ctx.alloc().buffer(arr.length);

        buf.writeBytes(arr);

        ctx.writeAndFlush(buf);
    }

    public void sendBack(ChannelHandlerContext ctx, byte arr){
        ByteBuf buf = ctx.alloc().buffer(1);
        buf.writeByte(arr);

        ctx.writeAndFlush(buf);
    }
}
