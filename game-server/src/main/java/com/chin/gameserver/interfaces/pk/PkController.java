package com.chin.gameserver.interfaces.pk;

import com.chin.gameserver.application.pk.IPkService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/19 19:53
 */
@RestController
public class PkController {

    @Resource
    private IPkService pkService;

    @PostMapping("/pk/start/game")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer aId = Integer.parseInt(data.getFirst("aId"));
        Integer aBotId = Integer.parseInt(data.getFirst("a_bot_id"));
        Integer bId = Integer.parseInt(data.getFirst("bId"));
        Integer bBotId = Integer.parseInt(data.getFirst("b_bot_id"));
        return pkService.startGame(aId, aBotId, bId, bBotId);
    }

    @PostMapping("/pk/receive/bot/move")
    public String receiveMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(data.getFirst("user_id"));
        Integer direction = Integer.parseInt(data.getFirst("direction"));
        return pkService.receiveNextMove(userId, direction);
    }
}
