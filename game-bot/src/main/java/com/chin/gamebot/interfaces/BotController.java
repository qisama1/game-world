package com.chin.gamebot.interfaces;

import com.chin.gamebot.application.IBotService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/29 11:50
 */
@RestController
public class BotController {

    @Resource
    private IBotService botService;

    @PostMapping("/bot/add/")
    public String addBot(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(data.getFirst("user_id"));
        String botCode = data.getFirst("bot_code");
        String input = data.getFirst("input");
        return botService.addBot(userId, botCode, input);
    }

}
