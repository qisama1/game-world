package com.chin.gameserver.application.server.socket;

import com.chin.gameserver.application.server.handlers.GameServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * @author qi
 */
public class NettyServer implements Callable<Channel> {

    private Logger logger = LoggerFactory.getLogger(NettyServer.class);

    /**
     * 一个boos group
     */
    private final EventLoopGroup boss = new NioEventLoopGroup(1);

    /**
     * worker group
     */
    private final EventLoopGroup work = new NioEventLoopGroup();

    /**
     * 生成的channel
     */
    private Channel channel;

    private ChannelFuture bing(InetSocketAddress ipAddress) {
        ChannelFuture channelFuture = null;
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work)
                    //非阻塞模式
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new GameServerInitializer());
            channelFuture = bootstrap.bind(ipAddress).sync();
        } catch (Exception e) {
            logger.info("game server started start error");
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("game server started");
            } else {
                logger.error("game server started start error");
            }
        }
        return channelFuture;
    }

    @Override
    public Channel call() throws Exception {
        // 监听7000端口的ws请求，在handler里面要特判
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 7000);
        ChannelFuture channelFuture = bing(inetSocketAddress);
        channel = channelFuture.channel();
        return channel;
    }
}
