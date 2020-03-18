package Connection.Handlers;

import Connection.CreatCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import workingWithMessage.SignIn;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    public enum State {
        IDLE, NAME_LENGTH, NAME, PASS_LENGTH, PASS, GET, NEXT
    }

    private State state = State.IDLE;
    private int nameLength;
    private String name;
    private int passLength;
    private String pass;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Поскольку этот хендлер стоит "первым от сети", то 100% получим ByteBuf
        ByteBuf buf = (ByteBuf)msg;
        while (buf.readableBytes()>0){
//            if(state == State.NEXT){
//                ctx.fireChannelRead(ctx);
//            }
            System.out.println(state);
            if(state == State.IDLE){
                byte readed = buf.readByte();
                if(readed == CreatCommand.getCommandAuth()){
                    state = State.NAME_LENGTH;
                } else {
                    System.out.println("ERROR: Invalid first byte - " + readed);
                }
            }

            if(state == State.NAME_LENGTH){
                if (buf.readableBytes() >= 4){
                    nameLength = buf.readInt();
                    state = State.NAME;
                }
            }

            if(state == State.NAME){
                if(buf.readableBytes() >=nameLength) {
                    byte[] nameByte = new byte[nameLength];
                    buf.readBytes(nameByte);
                    name = new String(nameByte, "UTF-8");
                    state = State.PASS_LENGTH;
                }
            }

            if(state == State.PASS_LENGTH){
                    if(buf.readableBytes() >= 4){
                        passLength = buf.readInt();
                        state = State.PASS;
                    }
            }

            if(state == State.PASS){
                if(buf.readableBytes() >= passLength) {
                    byte[] passByte = new byte[passLength];
                    buf.readBytes(passByte);
                    pass = new String(passByte, "UTF-8");
                    state = State.GET;
                }
            }

            if(state == State.GET){
                boolean checkAuth = SignIn.checkUser(name,pass);

                buf = ctx.alloc().buffer(1);
                System.out.println(name+" "+pass+ " " +checkAuth);
                System.out.println("here");
                if(checkAuth){
                    //в случае удачной авторизации удаляем этом handler и создаем, который обрабатыват команды
                    buf.writeByte(CreatCommand.getCommandAuthOk());

                    ctx.pipeline().addLast(new FileHandler());//, new EditFileHandler());
                    ctx.pipeline().remove(this);

                    ctx.writeAndFlush(buf);
                    state = State.NEXT;
                } else {
                    state = State.IDLE;
                    buf.writeByte(CreatCommand.getCommandAuthNok());
                    ctx.writeAndFlush(buf);
                }
                break;
            }
            break;
        }
    }

    // Стандартный обработчик исключений. Не забывайте его добавлять!!!
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
