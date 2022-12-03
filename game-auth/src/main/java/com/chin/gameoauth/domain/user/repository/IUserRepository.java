package com.chin.gameoauth.domain.user.repository;


import com.chin.gameoauth.domain.user.model.UserVO;

/**
 * @author qi
 * @description user仓储接口
 */
public interface IUserRepository {

    /**
     * 根据用户名查询User
     * @param username
     * @return
     */
    UserVO queryUserByName(String username);

    /**
     * 根据Id查询user
     * @param uId
     * @return
     */
    UserVO queryUserById(Integer uId);

    /**
     * 插入用户
     * @param username
     * @param password
     * @param photo
     * @param rating
     */
    void insertUser(String username, String password, String photo, Integer rating);


    /**
     * 更新用户
     * @param username
     * @param photo
     * @param rating
     * @return
     */
    boolean updateUser(String username, String photo, Integer rating);

    /**
     * 更改密码
     * @param password
     * @return
     */
    boolean updatePassword(String password);

    /**
     * 删除用户
     * @param uId
     * @return
     */
    boolean deleteUser(Integer uId);

}
