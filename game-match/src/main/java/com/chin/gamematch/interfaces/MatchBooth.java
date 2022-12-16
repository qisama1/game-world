package com.chin.gamematch.interfaces;

import com.chin.gamematch.application.IMatchService;
import com.chin.gamematch.rpc.IMatchBooth;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/14 20:50
 */
@Service(version = "1.0.0", retries = 1, timeout = 30000)
public class MatchBooth implements IMatchBooth {

    Logger logger = LoggerFactory.getLogger(MatchBooth.class);

    @Resource
    private IMatchService matchService;

    @Override
    public String test(String test) {
        logger.info("get !");
        return test;
    }

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        logger.info("addPlayer {}", userId);
        return matchService.addPlayer(userId, rating, botId);
    }

    @Override
    public String removePlayer(Integer userId) {
        return matchService.removePlayer(userId);
    }


}
