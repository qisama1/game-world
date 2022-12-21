package com.chin.gameserver.domain.match;

import com.chin.gameserver.application.match.IMatchService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/16 18:55
 */
@Service
public class MatchService implements IMatchService {

    private GenericService genericService;

    public MatchService() {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("game-match");
        application.setQosEnable(false);

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");
        registry.setRegister(false);

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface("com.chin.gamematch.rpc.IMatchBooth");
        reference.setVersion("1.0.0");
        reference.setGeneric("true");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(application)
                .registry(registry)
                .reference(reference)
                .start();

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        genericService = cache.get(reference);
    }

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        Object result = genericService.$invoke("addPlayer", new String[]{"java.lang.Integer", "java.lang.Integer", "java.lang.Integer"}, new Object[]{userId, rating, botId});
        return (String) result;
    }

    @Override
    public String removePlayer(Integer userId) {
        Object result = genericService.$invoke("removePlayer", new String[]{"java.lang.Integer"}, new Object[]{userId});
        return (String) result;
    }
}
