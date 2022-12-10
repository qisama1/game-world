package com.chin.gameserver.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author qi
 */
public class GameServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 数据解码操作
        channel.pipeline().addLast("http-codec", new HttpServerCodec());
        channel.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        channel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());

        // 自定义接受数据的回应方式
        channel.pipeline().addLast(new SocketHandler());
    }
}
