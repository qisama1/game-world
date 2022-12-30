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
public class Consumer extends Thread{

    private Lock lock;
    private Condition condition;
    private Deque<BotVO> botVODeque;

    public Consumer(Lock lock, Condition condition, Deque<BotVO> botVODeque) {
        this.lock = lock;
        this.condition = condition;
        this.botVODeque = botVODeque;
    }

    @Override
    public void run() {
        while (true) {
            BotVO botVO = null;
            lock.lock();
            try {
                while (botVODeque.isEmpty()) {
                    condition.await();
                }
                botVO = botVODeque.pollFirst();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            // 这里要先解锁再执行，否则执行时间过长，一直要被阻塞住。
            consume(botVO);
        }
    }

    private void consume(BotVO botVO) {
        RunningBot runningBot = new RunningBot(botVO);
        runningBot.startWithTimeout(2000);
    }
}
