package Connection.Handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


// Идет после FirstHandler в конвеере
public class EditFileHandler extends ChannelInboundHandlerAdapter {
//    private AddFile addFile;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

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
}
