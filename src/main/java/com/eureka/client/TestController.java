package com.eureka.client;

import com.eureka.client.netty.NettyClient;
import com.eureka.client.support.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author techoneduan
 * @date 2018/12/13
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ServiceClient client;

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
        NettyClient.send("hello");
        return "re";
    }
}
