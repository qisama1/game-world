package com.chin.gameserver.handlers;

import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author qi
 */
@Component
public class SocketHandler extends ChannelInboundHandlerAdapter {

    //1. 多线程下是防注入的，只能自己去取，所以采用@Autowired作用在方法上的方式去创建私有对象。
    //2. 这个SocketHandler是每个连接私有的，所以我们把它和连接一一对应进行管理
    //3. 需要restTemplate去从auth那边获取信息和记录信息

    private static RestTemplate restTemplate;
}
