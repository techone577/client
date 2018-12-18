package com.eureka.client.service;

import com.eureka.client.model.constant.NettyHeader;
import com.eureka.client.model.constant.RedisConstants;
import com.eureka.client.model.entity.NettyRespEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author techoneduan
 * @date 2018/12/17
 */

@Service
public class RegistryNettyService extends AbstractNettyService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistryNettyService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void dealRequest (NettyRespEntity resp) {
        LOG.info("更新本地服务实例:{}",resp.getResponse());
        try {
            redisTemplate.opsForValue().set(RedisConstants.INSTANCE_CACHE, resp.getResponse());
            redisTemplate.expire(RedisConstants.INSTANCE_CACHE, 30, TimeUnit.DAYS);
        } catch (Exception e) {
            LOG.info("更新服务实例,redis异常:{}", e);
        }
    }

    @Override
    public boolean matching (String factor) {
        return NettyHeader.REGISTRY.equals(factor);
    }


}
