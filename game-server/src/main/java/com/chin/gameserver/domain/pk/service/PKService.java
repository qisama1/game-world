package com.chin.gameserver.domain.pk.service;

import com.chin.gameserver.application.pk.IPkService;
import com.chin.gameserver.application.server.handlers.SocketHandler;
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
}
