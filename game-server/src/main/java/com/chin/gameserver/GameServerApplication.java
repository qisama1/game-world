package com.chin.gameserver;

import com.chin.gameserver.socket.NettyServer;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import io.netty.channel.Channel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.concurrent.*;

/**
 * @author qi
 */
@SpringBootApplication
public class GameServerApplication {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
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
