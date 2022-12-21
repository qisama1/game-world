package com.chin.gameserver.application.server.model;

import com.alibaba.fastjson.JSONObject;
import com.chin.gameserver.application.server.handlers.ChannelHandler;
import com.chin.gameserver.application.server.handlers.SocketHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/19 19:57
 */
public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final int[][] g;
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";
    private String loser = "";
    private Bot botA;
    private Bot botB;

    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB, Bot botA, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];

        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }
        playerA = new Player(idA, botIdA, botCodeA , rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, botCodeB, 1, cols - 2, new ArrayList<>());
        this.botA = botA;
        this.botB = botB;
        this.createMap();
    }

    public int[][] getG() {
        return g;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) {
            return true;
        }
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {  // 画地图
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) {
                    continue;
                }
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) {
                    continue;
                }

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++ ) {
            if (draw()){
                break;
            }
        }
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }


    private void sendBotCode(Player player) {
        if (player.getBotId() == -1) {
            return;
        }
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));
        SocketHandler.restTemplate.postForObject(addBotUrl, data, String.class);
    }

    /**
     * 编码当前的局面信息
     * @param player
     * @return
     */
    private String getInput(Player player) {
        // 地图#自己的起始横坐标#自己的纵坐标#自己的操作#对手的横坐标#对手的纵坐标#对手的操作
        Player me, you;
        if (player.getId().equals(playerA.getId())) {
            me = playerA;
            you = playerB;
        } else {
            you = playerA;
            me = playerB;
        }
        return getMap() + "#" + me.getSx() + "#" + me.getSy() + "#(" +
                me.getStepsString() + ")#" + you.getSx() + "#" + you.getSy() + "#(" + you.getStepsString() + ")";
    }

    /**
     * 等待下一步操作
     * @return
     */
    private boolean nextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        // 0, 1, 2, 3表示上下左右
        try {
            for (int i = 0; i < 50; i ++) {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
            System.out.println(nextStepB);
        } finally {
            lock.unlock();
        }
    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.x][cell.y] == 1)  {
            return false;
        }

        for (int i = 0 ; i < n - 1; i ++) {
            if (cellsA.get(i).x.equals(cell.x) && cellsA.get(i).y.equals(cell.y)) {
                return false;
            }
        }

        for (int i = 0 ; i < n; i ++) {
            if (cellsB.get(i).x.equals(cell.x) && cellsB.get(i).y.equals(cell.y)) {
                return false;
            }
        }
        return true;
    }

    private void judge() {
        List<Cell> cellsA = playerA.getCell();
        List<Cell> cellsB = playerB.getCell();

        // 判断墙
        // 判断A
        // 判断B
        boolean validA = checkValid(cellsA, cellsB);
        boolean validB = checkValid(cellsB, cellsA);

        if (!validA | !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    /**
     * 广播结果
     */
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        sendAllMessage(resp.toJSONString());
    }

    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            nextStepA = null;
            nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void sendAllMessage(String message) {
        if (ChannelHandler.userSockets.get(playerA.getId()) == null || ChannelHandler.userSockets.get(playerA.getId()) == null) {
            return;
        }
        ChannelHandler.userSockets.get(playerA.getId()).getChannelHandlerContext().writeAndFlush(new TextWebSocketFrame(message));
        ChannelHandler.userSockets.get(playerB.getId()).getChannelHandlerContext().writeAndFlush(new TextWebSocketFrame(message));
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i ++) {
            if (nextStep()) {
                judge();
                if (status.equals("playing")) {
                    System.out.println("sent");
                    sendMove();
                } else {
                    System.out.println("end");
                    sendResult();
                    saveToDB();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                saveToDB();
                break;
            }
        }
    }

    private void saveToDB() {
        User userA = ChannelHandler.userMap.get(playerA.getId());
        User userB = ChannelHandler.userMap.get(playerB.getId());
        if ("A".equals(loser)) {
            userA.setRating(userA.getRating() - 2);
            userB.setRating(userB.getRating() + 5);
        } else if ("B".equals(loser)) {
            userA.setRating(userA.getRating() + 5);
            userB.setRating(userB.getRating() - 2);
        }
//        System.out.println(userA.getRating());
//        MyServerHandler.userMapper.updateById(userA);
//        MyServerHandler.userMapper.updateById(userB);

        // 插入对局记录
//        Record record = new Record(
//                null,
//                playerA.getId(),
//                playerA.getSx(),
//                playerA.getSy(),
//                stepString(playerA.getSteps()),
//                playerB.getId(),
//                playerB.getSx(),
//                playerB.getSy(),
//                stepString(playerB.getSteps()),
//                getMap(),
//                loser
//        );
//        MyServerHandler.recordMapper.insert(record);
    }

    private String getMap() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j ++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private String stepString(List<Integer> steps) {
        StringBuilder sb = new StringBuilder();
        for (Integer step : steps) {
            sb.append(step);
        }
        return sb.toString();
    }
}