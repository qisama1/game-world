package com.chin.gameoauth.interfaces.controller;


import com.chin.gameoauth.application.IUserService;
import org.apache.ibatis.annotations.ResultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author qi
 */
@RestController
@CrossOrigin
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUserService userService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/user/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        logger.info("username: {}", map.get("username"));
        return userService.token(map.get("username"), map.get("password"));
    }

    @GetMapping("/user/account/info/")
    public Map<String, String> getInfo() {
        return userService.userInfo();
    }

}
