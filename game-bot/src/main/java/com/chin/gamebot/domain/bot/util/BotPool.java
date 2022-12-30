package com.chin.gamebot.domain.bot.util;

import com.chin.gamebot.domain.bot.model.BotVO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/29 12:21
 */
@Component
public class BotPool extends Thread{

    private Lock lock = new ReentrantLock();
    private Condition consumeCondition = lock.newCondition();
    private Deque<BotVO> botVODeque = new ArrayDeque<>();
    private Producer producer;
    private Consumer consumer;

    public static RestTemplate restTemplate;
    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        BotPool.restTemplate = restTemplate;
    }

    public void addBot(BotVO botVO) {
        this.producer.addBot(botVO);
    }

    @Override
    public void run() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        producer = new Producer(this.lock, this.consumeCondition, this.botVODeque);
        consumer = new Consumer(this.lock, this.consumeCondition, this.botVODeque);
        threadPoolExecutor.execute(consumer);
    }
}
