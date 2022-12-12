package com.chin.gameserver.application.server.handlers;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author qi
 * @description channel的统一管理
 */
public class ChannelHandler {

    /**
     * 存放所有channel
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
