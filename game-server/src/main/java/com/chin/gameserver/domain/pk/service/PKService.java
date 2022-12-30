package com.chin.gameserver.domain.pk.service;

import com.chin.gameserver.application.pk.IPkService;
import com.chin.gameserver.application.server.handlers.ChannelHandler;
import com.chin.gameserver.application.server.handlers.SocketHandler;
import com.chin.gameserver.application.server.model.Game;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/19 19:56
 */
@Service
public class PKService implements IPkService {

    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        SocketHandler.startGame(aId, aBotId, bId, bBotId);
        return "started";
    }

    @Override
    public String receiveNextMove(Integer userId, Integer direction) {
        SocketHandler socketHandler = ChannelHandler.userSockets.get(userId);
        Game game = socketHandler.game;
        if (game == null) {
            return "game finished";
        }
        if (game.getPlayerA().getId().equals(userId)) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(userId)) {
            game.setNextStepB(direction);
        }
        return "receiveBotMove success";
    }
}
