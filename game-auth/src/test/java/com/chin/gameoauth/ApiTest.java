package com.chin.gameoauth;


import com.chin.gameoauth.domain.user.model.UserVO;
import com.chin.gameoauth.domain.user.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
@SpringBootTest
public class ApiTest {

    private static final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Resource
    private IUserRepository userRepository;



    @Test
    public void testQuery() {
        UserVO userVO = userRepository.queryUserByName("aaa");
        logger.info("user: {}", userVO);
    }

    @Test
    public void testInsert() {
        userRepository.insertUser("chin", "123456", "nono", 1555);
    }

}
