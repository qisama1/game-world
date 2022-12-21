package com.chin.gameserver.application.server.handlers;

import com.chin.gameserver.application.server.model.User;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qi
 * @description channel的统一管理
 */
public class ChannelHandler {

    /**
     * 存放所有channel
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存放用户id与用户信息的map
     */
    public static Map<Integer, User> userMap = new ConcurrentHashMap<>();

    /**
     * 存放用户id与SocketChannel的map
     */
    public static Map<Integer, SocketHandler> userSockets = new ConcurrentHashMap<>();

}
