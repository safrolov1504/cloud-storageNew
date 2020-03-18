package Connection.Handlers;

import Connection.CreatCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// Идет после FirstHandler в конвеере
public class FileHandler extends ChannelInboundHandlerAdapter {


    public enum State {
        IDLE, NEXT_HANDLER,NAME_LENGTH, NAME, FILE_LENGTH, FILE, GET
    }
    private State state = State.IDLE;
    private int fileNameLength;
    private String fileName;
    private long fileLength;
    private long receivedFileLength;
    private String userName;

    private File file;
    private FileOutputStream fileOutputStream;

//    public FileHandler(String userName) {
//        this.userName = userName;
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("here");
        while (buf.readableBytes()>0){
            if(state == State.IDLE){
                byte read = buf.readByte();
                if(read == CreatCommand.getGetFile()){
                    state = State.NAME_LENGTH;
                } else {
                    //сделать переход в статус перекидования в следующий handler
                    state = State.NEXT_HANDLER;
                }
            }

            if(state == State.NEXT_HANDLER){
                //сделать переход в статус перекидования в следующий handler
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
            if(state == State.FILE_LENGTH){
                if(buf.readableBytes()>= 8){
                    fileLength = buf.readLong();

                    //getting ready for taking the file
                    //creat folders if it needs
                    file = new File("cloud-server/global-storage/"+userName);
                    if(!file.exists()){
                        file.mkdirs();
                    }

                    //creat file
                    file = new File(file.getPath()+"/"+ fileName);
                    file.createNewFile();
                    state = State.FILE;
                }
            }

            if(state == State.FILE){
                //addFile(ctx, buf);
                System.out.println(userName+" "+ fileName +" "+ fileLength);
                break;
            }
        }
    }

    private void addFile(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        while (buf.readableBytes()>0){
            fileOutputStream.write(buf.readByte());
            receivedFileLength++;
            if(receivedFileLength == fileLength){
                state = State.IDLE;
                sendBack(ctx, CreatCommand.getCommandAuthOk());
                break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void sendBack(ChannelHandlerContext ctx, byte arr){
        ByteBuf buf = ctx.alloc().buffer(1);
        buf.writeByte(arr);

        ctx.writeAndFlush(buf);
    }
}
