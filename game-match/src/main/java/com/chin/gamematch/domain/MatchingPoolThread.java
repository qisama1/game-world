package com.chin.gamematch.domain;

import com.chin.gamematch.domain.model.Player;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/15 18:28
 */
@Component
public class MatchingPoolThread extends Thread implements IMatchingPool{

    Logger logger = LoggerFactory.getLogger(MatchingPoolThread.class);
    private static final String START_GAME_URL = "http://127.0.0.1/game-server/pk/start/game";


    List<Player> players;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPoolThread.restTemplate = restTemplate;
    }

    public void setPlayers(List<Player> players) {
        writeLock.lock();
        try {
            this.players = players;
        } finally {
            writeLock.unlock();
        }
    }

    public MatchingPoolThread() {
        players = new ArrayList<>();
    }

    @Override
    public void run() {
        // 1. 给玩家新增一个退出属性，但是当时不立刻从列表中删除。
        // 2. 开启一个定时线程，专门从列表中把退出掉的线程给删除掉，这样就可以将用户取消匹配的复杂度降低。
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(3, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> {
            try {
                for (;;) {
                    sleep(10000);
                    logger.info("删除掉被排除的玩家");
                    List<Player> newPlayer = new ArrayList<>();
                    readLock.lock();
                    try {
                        for (Player player : players) {
                            if (!player.isQuited()) {
                                newPlayer.add(player);
                            }
                        }
                    } finally {
                        readLock.unlock();
                    }
                    writeLock.lock();
                    try {
                        setPlayers(newPlayer);
                    } finally {
                        writeLock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        singleThreadPool.execute(() -> {
            try {
                for (;;) {
                    logger.info("增加waiting time");
                    Thread.sleep(2000);
                    writeLock.lock();
                    try {
                        for (Player player : players) {
                            player.setWaitingTime(player.getWaitingTime() + 1);
                        }
                    } finally {
                        writeLock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        while (true) {
            try {
                Thread.sleep(1000);
                matchPlayers();
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    @Override
    public void addPlayer(Integer userId, Integer rating, Integer botId) {
        logger.info("我要加入了");
        writeLock.lock();
        try {
            Player player = new Player(userId, rating, 0, botId, false);
            players.add(player);
            playerMap.put(userId, player);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void remove(Integer userId) {
        writeLock.lock();
        try {
            playerMap.get(userId).setQuited(true);
            playerMap.remove(userId);
        } finally {
            writeLock.unlock();
        }
    }

    public void matchPlayers() {
        readLock.lock();
        Player playerA = null;
        Player playerB = null;
        try {
            for (int i = 0; i < players.size(); i ++) {
                if (players.get(i).isQuited()) {
                    continue;
                }
                for (int j = i + 1; j < players.size(); j ++) {
                    if (players.get(j).isQuited()) {
                        continue;
                    }
                    Player a = players.get(i);
                    Player b = players.get(j);
                    logger.info("a {}, b {}", a, b);
                    if (checkMatch(a, b)) {
                        logger.info("玩家A：{}， 玩家B：{} 匹配成功", a.getUserId(), b.getUserId());
                        playerA = a;
                        playerB = b;
                        sendResult(a, b);
                        break;
                    }
                }
            }
        } finally {
            readLock.unlock();
        }
        writeLock.lock();
        try {
            if (playerA == null || playerB == null) {
                logger.info("匹配失败，没有合适的玩家");
                return;
            }
            playerA.setQuited(true);
            playerB.setQuited(true);
        } finally {
            writeLock.unlock();
        }
    }

    private void sendResult(Player a, Player b) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("aId", a.getUserId().toString());
        data.add("bId", b.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        restTemplate.postForObject(START_GAME_URL, data, String.class);
    }

    /**
     * 判断是否匹配
     * @param a
     * @param b
     * @return
     */
    private boolean checkMatch(Player a, Player b) {
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }


}
