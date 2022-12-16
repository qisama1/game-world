package com.chin.gamematch.domain;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/15 19:52
 */
public interface IMatchingPool {

    /**
     * 添加用户到匹配池
     * @param userId
     * @param rating
     * @param botId
     */
    void addPlayer(Integer userId, Integer rating, Integer botId);

    /**
     * 把用户从线程池删除
     * @param userId
     */
    void remove(Integer userId);

}
