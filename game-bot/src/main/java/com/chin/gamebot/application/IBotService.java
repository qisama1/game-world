package com.chin.gamebot.application;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/29 11:47
 */
public interface IBotService {

    /**
     * 添加Bot进入排队队列
     * @param userId
     * @param botCode
     * @param input
     * @return
     */
    String addBot(Integer userId, String botCode, String input);

}
