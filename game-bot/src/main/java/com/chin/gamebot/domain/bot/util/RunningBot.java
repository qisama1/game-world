package com.chin.gamebot.domain.bot.util;

import com.chin.gamebot.domain.bot.model.BotVO;
import com.chin.gamebot.domain.bot.service.code.BotInterface;
import org.joor.Reflect;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/30 0:02
 */
public class RunningBot extends Thread{

    private BotVO bot;
    private final static String receiveBotMoveUrl = "http://127.0.0.1/game-server/pk/receive/bot/move";

    public RunningBot(BotVO botVO) {
        this.bot = botVO;
    }

    public void startWithTimeout(long timeout) {
        this.start();
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);

        BotInterface botIterface = Reflect.compile("com.chin.gamebot.domain.bot.service.code.Bot" + uid,
                addUid(bot.getBotCode(), uid)).create().get();
        Integer direction =  botIterface.nextMove(bot.getInput());
        System.out.println(bot.getUserId() + " " + direction);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());

        BotPool.restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }

    /**
     * 添加uuid
     * @param code
     * @param uId
     * @return
     */
    private String addUid(String code, String uId) {
        int k = code.indexOf(" implements BotInterface");
        return code.substring(0, k) + uId + code.substring(k);
    }
}
