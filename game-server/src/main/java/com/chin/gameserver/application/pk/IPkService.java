package com.chin.gameserver.application.pk;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/19 19:54
 */
public interface IPkService {

    /**
     * 开启游戏
     * @param aId
     * @param aBotId
     * @param bId
     * @param bBotId
     * @return
     */
    String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId);

}
