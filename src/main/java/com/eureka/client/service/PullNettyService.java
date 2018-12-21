package com.eureka.client.service;

import com.eureka.client.model.constant.NettyHeader;
import com.eureka.client.model.constant.RedisConstants;
import com.eureka.client.model.entity.NettyRespEntity;
import com.eureka.client.model.syncMap.SyncMap;
import com.eureka.client.support.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author techoneduan
 * @date 2018/12/21
 */
@Service
public class PullNettyService extends AbstractNettyService {

    private static final Logger LOG = LoggerFactory.getLogger(PullNettyService.class);

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void dealRequest (NettyRespEntity resp) {
        SyncMap.put(resp.getRequestId(), resp.getResponse());
        redisUtil.doCache(RedisConstants.INSTANCE_CACHE, resp.getResponse());
    }

    @Override
    public boolean matching (String factor) {
        return NettyHeader.PULL.equals(factor);
    }
}
