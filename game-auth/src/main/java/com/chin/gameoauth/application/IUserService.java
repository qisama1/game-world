package com.chin.gameoauth.application;

import java.util.Map;

/**
 * @author qi
 */
public interface IUserService {

    /**
     * 登录并分发JWT-token
     * @return
     */
    Map<String, String> token(String username, String password);

    /**
     * 获取用户信息
     * @return
     */
    Map<String, String> userInfo();

    /**
     * 注册用户
     * @param username
     * @param password
     * @param photo
     * @param rating
     * @return
     */
    Map<String, String> insertUser(String username, String password, String photo, Integer rating);

    /**
     * 更新用户
     * @param username
     * @param photo
     * @param rating
     * @return
     */
    Map<String, String> updateUser(String username, String photo, Integer rating);

    /**
     * 检查用户的JWT=token是否合法
     * @param token
     * @return
     */
    Map<String, String> checkUserToken(String token);

    /**
     *
     * @param userId
     * @return
     */
    Map<String, String> queryUserById(Integer userId);

    /**
     * 删除用户
     * @param uId
     * @return
     */
    Map<String, String> deleteUser(Integer uId);
}
