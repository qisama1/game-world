package com.chin.gamematch;

import com.chin.gamematch.domain.MatchingPoolThread;
import com.chin.gamematch.domain.service.MatchService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/14 20:33
 */
@SpringBootApplication
@Configurable
@EnableDubbo
public class GameMatchApplication {

    public static void main(String[] args) {
        MatchService.matchingPool.start();
        SpringApplication.run(GameMatchApplication.class, args);
    }
}
