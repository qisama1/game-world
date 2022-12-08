package com.chin.gameoauth.domain.user.service;

import com.chin.gameoauth.application.IUserService;
import com.chin.gameoauth.domain.security.model.UserDetailsImpl;
import com.chin.gameoauth.domain.security.model.VO.UserDetail;
import com.chin.gameoauth.domain.security.util.JwtUtil;
import com.chin.gameoauth.domain.user.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qi
 */
@Service
public class UserService implements IUserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> token(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // 1. 让Security去完成登录
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl user = (UserDetailsImpl) authenticate.getPrincipal();
        UserDetail userDetail = user.getUserDetail();

        // 2. 生成jwt-token
        String jwt = JwtUtil.createJWT(userDetail.getId().toString());
        logger.info("user: {} , token: {}", userDetail, jwt);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("error_message", "success");
        resultMap.put("token", jwt);
        return resultMap;
    }

    @Override
    public Map<String, String> userInfo() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        UserDetail userDetail = user.getUserDetail();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("error_message", "success");
        resultMap.put("id", userDetail.getId().toString());
        resultMap.put("username", userDetail.getUsername());
        resultMap.put("photo", userDetail.getPhoto());
        resultMap.put("rating", userDetail.getRating().toString());
        return resultMap;
    }

    @Override
    public Map<String, String> insertUser(String username, String password, String photo, Integer rating) {
        Map<String, String> resultMap = new HashMap<>();
        if (null == username || "".equals(username)) {
            resultMap.put("error_message", "用户名不能为空");
            return resultMap;
        }
        if (null == password || "".equals(password)) {
            resultMap.put("error_message", "密码不能为空");
            return resultMap;
        }
        userRepository.insertUser(username,
                password,
                photo,
                rating);

        resultMap.put("error_message", "success");
        return resultMap;
    }

    @Override
    public Map<String, String> updateUser(String username, String photo, Integer rating) {
        boolean success = userRepository.updateUser(username, photo, rating);
        Map<String, String> resultMap = new HashMap<>();
        if (success) {
            resultMap.put("error_message", "success");
        } else {
            resultMap.put("error_message", "delete failed");
        }
        return resultMap;
    }

    @Override
    public Map<String, String> checkUserToken(String token) {
        return null;
    }

    @Override
    public Map<String, String> deleteUser(Integer uId) {
        boolean success = userRepository.deleteUser(uId);
        Map<String, String> resultMap = new HashMap<>();
        if (success) {
            resultMap.put("error_message", "success");
        } else {
            resultMap.put("error_message", "delete failed");
        }
        return resultMap;
    }


}
