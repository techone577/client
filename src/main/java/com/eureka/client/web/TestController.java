package com.eureka.client.web;

import com.eureka.client.model.constant.NettyHeader;
import com.eureka.client.model.entity.NettyReqEntity;
import com.eureka.client.netty.ServiceClient;
import com.eureka.client.support.annotation.ServiceInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author techoneduan
 * @date 2018/12/13
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/test")
    @ServiceInfo(name = "BLOG.TestController.test",description = "测试方法注册")
    public String test(){
//        String res = client.feignTest();
        return "hello";
    }

    @RequestMapping(value = "/test2",method = RequestMethod.POST)
    @ServiceInfo(name = "BLOG.TestController.test",description = "测试方法注册")
    public String test2(){
//        String res = client.feignTest();
//        return res;
        NettyReqEntity entity = new NettyReqEntity();
        entity.setHeader(NettyHeader.REQUEST);
        entity.setParams("test");
        return ServiceClient.call(entity);
    }
}
