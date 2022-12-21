package com.chin.gameoauth;


import com.chin.gameoauth.domain.user.model.UserVO;
import com.chin.gameoauth.domain.user.repository.IUserRepository;
import com.chin.gameoauth.infrastructure.dao.IBotDao;
import com.chin.gameoauth.infrastructure.pojo.Bot;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class ApiTest {

    private static final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IBotDao botDao;

    @Test
    public void testQuery() {
        UserVO userVO = userRepository.queryUserByName("aaa");
        logger.info("user: {}", userVO);
    }

    @Test
    public void testInsert() {
        userRepository.insertUser("chin", "123456", "nono", 1555);
    }

    @Test
    public void testBot() {
        List<Bot> bots = botDao.queryBotByUserId(3);
        logger.info("{}", bots);
    }

    @Test
    public void testBotId(){
    }
}
