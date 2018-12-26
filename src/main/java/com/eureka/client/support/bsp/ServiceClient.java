package com.eureka.client.support.bsp;

import com.eureka.client.model.constant.Constants;
import com.eureka.client.model.constant.NettyHeader;
import com.eureka.client.model.constant.RedisConstants;
import com.eureka.client.model.constant.RestMethod;
import com.eureka.client.model.entity.NettyReqEntity;
import com.eureka.client.model.entity.ServiceEntity;
import com.eureka.client.model.enums.ErrorCodeEnum;
import com.eureka.client.model.syncMap.SyncMap;
import com.eureka.client.netty.NettyClient;
import com.eureka.client.support.exception.UnifiedException;
import com.eureka.client.support.interceptor.RestRequestInterceptor;
import com.eureka.client.support.utils.Base64Util;
import com.eureka.client.support.utils.JsonUtil;
import com.eureka.client.model.entity.Response;
import com.eureka.client.support.utils.RedisUtil;
import com.eureka.client.support.utils.ResponseBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;

/**
 * @author techoneduan
 * @date 2018/12/15
 * <p>
 * 服务调用工具
 */
@Component
public class ServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceClient.class);

    private final RestTemplate restTemplate = new RestTemplate() {
        {
            setInterceptors(Collections.singletonList(new RestRequestInterceptor()));
        }
    };

    @Autowired
    private RedisUtil redisUtil;

    public Response call (String serviceName, String params) {
        String response = excuteCall(serviceName, params);
        return ResponseBuilder.build(true, response);
    }

    private String excuteCall (String serviceName, String params) {
        List<ServiceEntity> serviceList = pullServiceFromLocal();
        if (null == serviceList || serviceList.size() == 0)
            throw new UnifiedException(ErrorCodeEnum.NO_SERVICE_AVAILABLR);
        /**
         * TODO 超时重试、负载均衡
         */
        Optional<ServiceEntity> optional = serviceList.stream().filter(item -> item.getServiceName().equals(serviceName)).findAny();
        if (null == optional || null == optional.get())
            throw new UnifiedException(ErrorCodeEnum.NO_SERVICE_AVAILABLR);
        ServiceEntity service = optional.get();
        String resp = excuteByMethod(service, params);
        return resp;
    }

    private String excuteByMethod (ServiceEntity service, String params) {
        String resp = null;
        String url = Constants.HTTP_PREFIX + service.getIpAddr() + Constants.PATH_SEPERATOR + service.getPort() + service.getRouteAddr();
        params = Base64Util.encodeToUrlSafeString(params);
        if (RestMethod.POST.equalsIgnoreCase(service.getMethod())) {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, params, String.class);
            resp = responseEntity.getBody();
        } else if (RestMethod.GET.equalsIgnoreCase(service.getMethod())) {
            /**
             * 避免restTemplate强制url encode
             */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url + Constants.QUESTION_MARK + params);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriBuilder.build().toUri(), String.class);
            resp = responseEntity.getBody();
        }
        return resp;
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
