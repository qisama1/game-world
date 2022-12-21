package com.chin.gamematch;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/14 20:43
 */
public class ApiTest {

    private RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);
    @Test
    public void test_rpc() {

        ApplicationConfig application = new ApplicationConfig();
        application.setName("game-match");
        application.setQosEnable(false);

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");
        registry.setRegister(false);

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface("com.chin.gamematch.rpc.IMatchBooth");
        reference.setVersion("1.0.0");
        reference.setGeneric("true");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(application)
                .registry(registry)
                .reference(reference)
                .start();

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);

        Object result = genericService.$invoke("addPlayer", new String[]{"java.lang.Integer", "java.lang.Integer", "java.lang.Integer"}, new Object[]{1, 1, 1});

        System.out.println(result);
    }

    @Test
    public void testBotId() {
        String checkTokenUrl = "http://game-auth/user/bot/queryBot";
        MultiValueMap<String, Integer> data = new LinkedMultiValueMap<>();
        data.add("botId", 3);
        HashMap<String, String> resultMap = restTemplate.postForObject(checkTokenUrl, data, HashMap.class);
        logger.info("{}", resultMap);
    }
}
