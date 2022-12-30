package com.chin.gamebot.domain.bot.service;

import com.chin.gamebot.application.IBotService;
import com.chin.gamebot.domain.bot.model.BotVO;
import com.chin.gamebot.domain.bot.util.BotPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/29 11:51
 */
@Service
@Order
public class BotService implements IBotService, ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(BotService.class);
    private final BotPool botPool = new BotPool();

    @Override
    public String addBot(Integer userId, String botCode, String input) {
        // 加入bot进入排队队列当中，同时要有一个线程去监控排队列队
        logger.info("user:{} 加入排队列队", userId);
        botPool.addBot(new BotVO(userId, botCode, input));
        return "add bot success";
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 这一块要启动一个监控排队队列的线程
        botPool.start();
    }
}
