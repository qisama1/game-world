package com.chin.gamebot.domain.bot.service.code;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/29 12:06
 */
public interface BotInterface {

    /**
     * 返回下一步
     * @param input
     * @return
     */
    Integer nextMove(String input);
}
