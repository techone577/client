package com.eureka.client.netty;

import com.eureka.client.model.constant.NettyHeader;
import com.eureka.client.model.entity.NettyReqEntity;
import com.eureka.client.model.entity.NettyRespEntity;
import com.eureka.client.model.eureka.RegistryInfo;
import com.eureka.client.support.spring.ApplicationContextCache;
import com.eureka.client.model.eureka.ServiceConfig;
import com.eureka.client.support.utils.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * @author techoneduan
 * @date 2018/12/15
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandler.class);

    private ByteBuf MSG;

    private static ChannelFuture future = null;


    public NettyClientHandler () {
        //注册方法信息
        NettyReqEntity nettyReqEntity = new NettyReqEntity();
        RegistryInfo registryInfo = ApplicationContextCache.getRegistryInfo();
        nettyReqEntity.setRequestId(UUID.randomUUID().toString());
        nettyReqEntity.setHeader(NettyHeader.REGISTRY);
        nettyReqEntity.setParams(JsonUtil.toString(registryInfo));
        if (null != registryInfo.getServiceConfigs() && registryInfo.getServiceConfigs().size() > 0) {
            byte[] req = JsonUtil.toString(nettyReqEntity).getBytes();
            MSG = Unpooled.buffer(req.length);
            MSG.writeBytes(req);
        }
    }

    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(MSG);
    }

    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        LOG.info("response:{}", body);
        NettyRespEntity respEntity = JsonUtil.toBean(body, NettyRespEntity.class);
//        if (!SyncMap.hasKey(respEntity.getRequestId())) {
//            SyncMap.put(respEntity.getRequestId(), respEntity.getResponse());
//        }
        if (respEntity == null)
            return;
        ApplicationContextCache.getFactoryListHolder().getNettyService().getBean(respEntity.getRespType()).dealRequest(respEntity);

    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
