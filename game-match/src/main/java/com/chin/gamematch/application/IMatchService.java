package com.chin.gamematch.application;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/15 18:24
 */
public interface IMatchService {

    /**
     * 新增用户 （用户开始匹配）
     * @param userId
     * @param rating
     * @param botId
     * @return
     */
    String addPlayer(Integer userId, Integer rating, Integer botId);

    /**
     * 删除用户（用户取消匹配）
     * @param userId
     * @return
     */
    String removePlayer(Integer userId);

}
