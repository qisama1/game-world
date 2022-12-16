package com.chin.gamematch.domain.service;

import com.chin.gamematch.application.IMatchService;
import com.chin.gamematch.domain.MatchingPoolThread;
import org.apache.dubbo.config.annotation.Service;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/15 18:24
 */
@Service
public class MatchService implements IMatchService {

    public final static MatchingPoolThread matchingPool = new MatchingPoolThread();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        matchingPool.addPlayer(userId, rating, botId);
        return "added";
    }

    @Override
    public String removePlayer(Integer userId) {
        matchingPool.remove(userId);
        return "removed";
    }
}
