package com.eureka.client.netty;

import com.eureka.client.support.ApplicationContextCache;
import com.eureka.client.support.ServiceConfig;
import com.eureka.client.support.utils.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * @author techoneduan
 * @date 2018/12/15
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private  ByteBuf firstMSG;

    public NettyClientHandler () {
        //注册方法信息
        List<ServiceConfig> list = ApplicationContextCache.getServiceConfig();
        if (null != list && list.size() > 0){
            byte[] req = JsonUtil.toString(list).getBytes();
            firstMSG = Unpooled.buffer(req.length);
            firstMSG.writeBytes(req);
        }
    }

    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMSG);
    }

    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("NOW is: " + body);

    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
