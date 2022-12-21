package com.chin.gameserver.application.server.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chin.gameserver.application.match.IMatchService;
import com.chin.gameserver.application.server.model.Bot;
import com.chin.gameserver.application.server.model.Game;
import com.chin.gameserver.application.server.model.User;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qi
 */
@Component
public class SocketHandler extends ChannelInboundHandlerAdapter {

    //1. 多线程下是防注入的，只能自己去取，所以采用@Autowired作用在方法上的方式去创建私有对象。
    //2. 这个SocketHandler是每个连接私有的，所以我们把它和连接一一对应进行管理
    //3. 需要restTemplate去从auth那边获取信息和记录信息

    private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    public ChannelHandlerContext ctx = null;

    /**
     * 存放当前channel对应的用户
     */
    private User user = null;

    /**
     * 存放当前channel对应的game
     */
    private Game game = null;

    /**
     * 存放当前channel的jwtToken
     */
    private String jwtToken = null;

    /**
     * 当前channel的websocket握手
     */
    private WebSocketServerHandshaker handshaker;

    public static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        SocketHandler.restTemplate = restTemplate;
    }

    private static IMatchService matchService;
    @Autowired
    public void setMatchService(IMatchService matchService) {
        SocketHandler.matchService = matchService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("{} 建立连接", ctx.channel().remoteAddress());
        this.ctx = ctx;
        ChannelHandler.channelGroup.add(ctx.channel());
        // 如果需要可以做一个广播通知，这个xxx用户上线游戏大厅。
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("{} 断开连接", ctx.channel().remoteAddress());
        this.ctx = null;
        ChannelHandler.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 1. 我们需要判断如果是FullHttpRequest，说明还没有转到WebSocket协议，那么这是第一次，我们要处理握手
        // 2. 如果已经转换到了WebSocket协议，那么后续的信息我们就要根据业务逻辑来判断了
        // 3. 如果是请求匹配请求，那么就要把它加入用户池（交由匹配系统）
        // 4. 如果是对局内容就进行move操作，修改对局动作

        // http请求，说明还没有转换协议
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            if (!httpRequest.decoderResult().isSuccess()) {
                DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                // 返回应答给客户端
                if (httpResponse.status().code() != 200) {
                    ByteBuf buf = Unpooled.copiedBuffer(httpResponse.status().toString(), CharsetUtil.UTF_8);
                    httpResponse.content().writeBytes(buf);
                    buf.release();
                }
                // 如果是非Keep-Alive，关闭连接
                ChannelFuture f = ctx.channel().writeAndFlush(httpResponse);
                if (httpResponse.status().code() != 200) {
                    f.addListener(ChannelFutureListener.CLOSE);
                }
                return;
            }
            // 进行ws握手，进行协议升级
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
            handshaker = wsFactory.newHandshaker(httpRequest);
            if (null == handshaker) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), httpRequest);
            }
            return;
        }

        // ws请求，这时候就处理业务逻辑了
        if (msg instanceof WebSocketFrame) {
            WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
            // 如果是关闭请求
            if (webSocketFrame instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) webSocketFrame.retain());
                return;
            }
            // 如果是ping请求，回复pong
            if (webSocketFrame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
                return;
            }
            if (!(webSocketFrame instanceof TextWebSocketFrame)) {
                throw new Exception("本服务器只支持文本格式");
            }
            // 得到请求的具体内容，这是一个json文本
            String request = ((TextWebSocketFrame) webSocketFrame).text();
            // 解析json对象
            JSONObject data = JSON.parseObject(request);
            String token = data.getString("token");
            String event = data.getString("event");
            if (token != null) {
                jwtToken = token;
                token = "Bearer ".concat(token);
                user = getUserInfo(token);
                ChannelHandler.userSockets.put(user.getId(), this);
                ChannelHandler.userMap.put(user.getId(), user);
                logger.info("服务器收到来自 {} 的 user: 的{}", ctx.channel().remoteAddress(), user.getUsername(),request);
            }
            if (event != null) {
                if ("start-matching".equals(event)) {
                    logger.info("user:{} 开始匹配 使用bot为{}", user.getUsername(), data.getInteger("bot_id"));
                    startMatch(data.getInteger("bot_id"));
                } else if ("stop-matching".equals(event)){
                    logger.info("user:{} 结束匹配", user.getUsername());
                    stopMatch();
                } else if ("move".equals(event)) {
                    logger.info("move {}", data.getInteger("direction"));
                }
            }
        }
    }

    /**
     * 从game-auth获取用户信息
     * @param token
     * @return
     * @throws Exception
     */
    private User getUserInfo(String token) throws Exception {
        // 此时需要转发到game-auth去获取用户信息
        String checkTokenUrl = "http://game-auth/user/account/info";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        HashMap<String, String> resultMap = restTemplate.exchange(checkTokenUrl, HttpMethod.GET, entity, HashMap.class).getBody();
        if (! resultMap.get("error_message").equals("success")) {
            throw new Exception("登录过期，请重新登录");
        }
        return new User(Integer.parseInt(resultMap.get("id")), resultMap.get("username"), resultMap.get("photo"), Integer.valueOf(resultMap.get("rating")));
    }

    private void startMatch(Integer botId) {
        // 这里交由game-match负责，然后它会匹配好了对局以后交由server进行对局执行
        matchService.addPlayer(user.getId(), user.getRating(), botId);
    }

    private void stopMatch() {
        matchService.removePlayer(user.getId());
    }

    private void move() {
        // 记录下用户的移动
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return this.ctx;
    }

    /**
     * 接受到匹配服务器的信息后，再此开始对局
     * @param aId
     * @param aBotId
     * @param bId
     * @param bBotId
     */
    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        // 开启对局
        logger.info("start!, {}, {}", aId, bId);
        User a = ChannelHandler.userSockets.get(aId).user;
        User b = ChannelHandler.userSockets.get(bId).user;
        Bot botA = null;
        if (aBotId.equals(-1)) {
            botA = new Bot();
            botA.setId(-1);
        } else {
            botA = postIdForBot(aBotId);
        }
        Bot botB = null;
        if (bBotId.equals(-1)) {
            botB = new Bot();
            botB.setId(-1);
        } else {
            botB = postIdForBot(bBotId);
        }
        Map<Integer, SocketHandler> users = ChannelHandler.userSockets;
        Game game = new Game(13, 14, 20, a.getId(), b.getId(), botA, botB);
        users.get(a.getId()).game = game;
        users.get(b.getId()).game = game;
        game.start();

        JSONObject respGame = buildGameResponse(game);
        JSONObject respA = buildGameResponseDetail(respGame, b);

        if (users.get(a.getId()) != null) {
            users.get(a.getId()).ctx.channel().writeAndFlush(new TextWebSocketFrame(respA.toJSONString()));
        }

        JSONObject respB = buildGameResponseDetail(respGame, a);
        if (users.get(b.getId()) != null) {
            users.get(b.getId()).ctx.channel().writeAndFlush(new TextWebSocketFrame(respB.toJSONString()));
        }
    }

    private static JSONObject buildGameResponse(Game game) {
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());
        return respGame;
    }

    private static JSONObject buildGameResponseDetail(JSONObject respGame, User another) {
        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", another.getUsername());
        respA.put("opponent_photo", another.getPhoto());
        respA.put("game", respGame);
        return respA;
    }

    private static Bot postIdForBot(Integer botId) {
        String checkTokenUrl = "http://game-auth/user/bot/queryBot";
        MultiValueMap<String, Integer> data = new LinkedMultiValueMap<>();
        data.add("botId", botId);
        HashMap<String, String> resultMap = restTemplate.postForObject(checkTokenUrl, data, HashMap.class);
        return new Bot(Integer.valueOf(resultMap.get("botId")), Integer.valueOf(resultMap.get("userId")), resultMap.get("content"), resultMap.get("description"), resultMap.get("title"));
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
