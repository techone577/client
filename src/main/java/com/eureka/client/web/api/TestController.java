package com.eureka.client.web.api;

import com.eureka.client.model.dto.SignInReqDTO;
import com.eureka.client.support.annotation.Json;
import com.eureka.client.support.bsp.ServiceClient;
import com.eureka.client.support.annotation.ServiceInfo;
import com.eureka.client.model.entity.Response;
import com.eureka.client.support.utils.JsonUtil;
import com.eureka.client.support.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ServiceClient caller;

    @RequestMapping(value = "/test")
    @ServiceInfo(name = "BLOG.TestController.test", description = "测试方法注册")
    public Response test () {
        SignInReqDTO reqDTO = new SignInReqDTO();
        reqDTO.setRootPassword("tetetete");
        reqDTO.setRootUserName("rererer");
        return caller.call("blogging.test", JsonUtil.toString(reqDTO));
    }

    @RequestMapping(value = "/test2", method = RequestMethod.POST)
    @ServiceInfo(name = "BLOG.TestController.test2", description = "测试方法注册")
    public String test2 () {
        return null;
    }

    @RequestMapping(value = "/sss")
    public Response test3 (@Json SignInReqDTO dto) {
        String s = dto.getRootPassword();
        return ResponseBuilder.build(true, dto);
    }

}
