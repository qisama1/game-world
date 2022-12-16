package com.chin.gamematch.rpc;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/14 20:49
 */
public interface IMatchBooth {

    /**
     * 测试RPC
     * @param test
     * @return
     */
    String test(String test);

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
