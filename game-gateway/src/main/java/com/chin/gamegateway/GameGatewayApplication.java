package com.chin.gamegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author qi
 */
@SpringBootApplication
public class GameGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameGatewayApplication.class, args);
    }

}
