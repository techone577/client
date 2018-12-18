package com.eureka.client.service;


import com.eureka.client.model.entity.NettyRespEntity;
import com.eureka.client.support.strategy.MatchingBean;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author techoneduan
 * @date 2018/12/17
 */
public abstract class AbstractNettyService implements MatchingBean<String> {

    public abstract void dealRequest (NettyRespEntity resp);

}
