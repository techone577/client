package com.eureka.client.support.bsp;

import com.eureka.client.model.constant.NettyHeader;
import com.eureka.client.model.constant.RedisConstants;
import com.eureka.client.model.entity.NettyReqEntity;
import com.eureka.client.model.entity.ServiceEntity;
import com.eureka.client.model.enums.ErrorCodeEnum;
import com.eureka.client.model.syncMap.SyncMap;
import com.eureka.client.netty.NettyClient;
import com.eureka.client.support.exception.UnifiedException;
import com.eureka.client.support.utils.JsonUtil;
import com.eureka.client.model.entity.Response;
import com.eureka.client.support.utils.RedisUtil;
import com.eureka.client.support.utils.ResponseBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author techoneduan
 * @date 2018/12/15
 * <p>
 * 服务调用工具
 */
@Component
public class ServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceClient.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RedisUtil redisUtil;

    public Response call (String serviceName, String params) {
        String reqAddr = buildRequestURL(serviceName);
        ResponseEntity<String> resp = restTemplate.getForEntity(reqAddr, String.class);
        return ResponseBuilder.build(true, resp.getBody());
    }

    private String buildRequestURL (String serviceName) {
        List<ServiceEntity> serviceList = pullServiceFromLocal();
        if (null == serviceList || serviceList.size() == 0)
            throw new UnifiedException(ErrorCodeEnum.NO_SERVICE_AVAILABLR);
        Optional<ServiceEntity> optional = serviceList.stream().filter(item -> item.getServiceName().equals(serviceName)).findAny();
        if (null == optional || null == optional.get())
            throw new UnifiedException(ErrorCodeEnum.NO_SERVICE_AVAILABLR);
        ServiceEntity service = optional.get();
        String reqAddr = "http://" + service.getIpAddr() + ":" + service.getPort() + service.getRouteAddr();
        return reqAddr;
    }

    private List<ServiceEntity> pullServiceFromLocal () {
        List<ServiceEntity> list = null;
        String services = redisUtil.getString(RedisConstants.INSTANCE_CACHE);
        if (StringUtils.isBlank(services))
            services = pullServiceFromRemote();

        if (StringUtils.isBlank(services))
            throw new UnifiedException(ErrorCodeEnum.NO_SERVICE_AVAILABLR);
        List<ServiceEntity> serviceList = JsonUtil.toList(services, ServiceEntity.class);
        return serviceList;
    }

    private String pullServiceFromRemote () {
        NettyReqEntity entity = new NettyReqEntity();
        entity.setRequestId(UUID.randomUUID().toString());
        entity.setHeader(NettyHeader.PULL);
        NettyClient.send(JsonUtil.toString(entity));
        while (!SyncMap.hasKey(entity.getRequestId())) {
            //TODO 设置超时时间
            LOG.info("wait!");
        }
        return SyncMap.get(entity.getRequestId());
    }

}
