package com.eureka.client;

import com.eureka.client.netty.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ClientApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ClientApplication.class);
    public static void main (String[] args) {
        SpringApplication.run(ClientApplication.class, args);

        try {
            NettyClient.connect();
        } catch (Exception e) {
            LOG.info("连接netty服务器失败...");
        }
        LOG.info("netty启动结束...");
    }
}
