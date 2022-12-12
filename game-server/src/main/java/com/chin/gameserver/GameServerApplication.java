package com.chin.gameserver;

import com.chin.gameserver.application.server.socket.NettyServer;
import io.netty.channel.Channel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;

/**
 * @author qi
 */
@SpringBootApplication
public class GameServerApplication {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        // 非自定义线程池p3c会报错，选用大小为2的fixed类型线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        Future<Channel> future = threadPoolExecutor.submit(nettyServer);
        try {
            Channel channel = future.get();
            while (!channel.isActive()) {
                // 等待到Netty服务器开启
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        SpringApplication.run(GameServerApplication.class, args);
    }
}
