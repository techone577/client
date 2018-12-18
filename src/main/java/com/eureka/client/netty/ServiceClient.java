package com.eureka.client.netty;

import com.eureka.client.model.entity.NettyReqEntity;
import com.eureka.client.model.syncMap.SyncMap;
import com.eureka.client.support.utils.JsonUtil;

import java.util.UUID;

/**
 * @author techoneduan
 * @date 2018/12/18
 */
//服务调用
public class ServiceClient {

    /**
     * 取本地缓存调用
     *
     */
    public static String call (NettyReqEntity msg) {
        msg.setRequestId(UUID.randomUUID().toString());
        NettyClient.send(JsonUtil.toString(msg));
        //TODO 超时设置
        while(!SyncMap.hasKey(msg.getRequestId())){
            System.out.println("no");
        }
        return SyncMap.get(msg.getRequestId());
    }
}
