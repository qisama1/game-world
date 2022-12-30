package com.chin.gamebot.domain.bot.util;

import com.chin.gamebot.domain.bot.model.BotVO;

import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/29 13:17
 */
public class Producer {

    private Lock lock;
    private Condition condition;
    private Deque<BotVO> botVODeque;

    public Producer(Lock lock, Condition condition, Deque<BotVO> botVODeque) {
        this.lock = lock;
        this.condition = condition;
        this.botVODeque = botVODeque;
    }

    public void addBot(BotVO botVo) {
        lock.lock();
        try {
            botVODeque.addLast(botVo);
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
