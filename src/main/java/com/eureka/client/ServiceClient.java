package com.eureka.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author techoneduan
 * @date 2018/12/13
 */

@FeignClient(name = "test-service")
public interface ServiceClient {

    @RequestMapping(value = "/test/serviceTest")
    String feignTest();
}
