package com.chin.gamegateway;

import com.alibaba.nacos.common.utils.StringUtils;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class ApiTest {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Autowired
    private RestTemplate restTemplate;

    private String serviceName = "game-gateway";

    @Test
    public void testNacos(){
        String res = restTemplate.getForObject("http://" + serviceName + "/game-auth/user/account/info", String.class);
        System.out.println(res);
    }
}
