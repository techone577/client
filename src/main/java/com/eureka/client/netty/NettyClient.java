package com.eureka.client.netty;

import com.eureka.client.support.utils.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @author techoneduan
 * @date 2018/12/15
 */
public class NettyClient {

    private static final String host = "127.0.0.1";
    private static final Integer port = 1002;

    private static EventLoopGroup group = null;
    private static Bootstrap client = null;
    private static ChannelFuture future = null;

    public static void connect () throws Exception {
        //配置客户端NIO 线程组
        group = new NioEventLoopGroup();
        client = new Bootstrap();
        try {
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel (SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            //绑定端口, 异步连接操作
            future = client.connect(host, port).sync();

            //等待客户端连接端口关闭
            future.channel().closeFuture().sync();
        } finally {
            //优雅关闭 线程组
            group.shutdownGracefully();
        }
    }

    public static void send (String str) {
        ByteBuf MSG;
        byte[] req = str.getBytes();
        MSG = Unpooled.buffer(req.length);
        MSG.writeBytes(req);
        if (null != future)
            future.channel().writeAndFlush(MSG);
    }
}
