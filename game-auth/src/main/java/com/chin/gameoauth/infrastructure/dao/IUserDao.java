package com.chin.gameoauth.infrastructure.dao;

import com.chin.gameoauth.infrastructure.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qi
 */
@Mapper
public interface IUserDao {

    /**
     * 根据用户名查询User
     * @param username
     * @return
     */
    User queryUserByName(String username);

    /**
     * 根据Id查询user
     * @param uId
     * @return
     */
    User queryUserById(Integer uId);


    /**
     * 新增用户
     * @param user
     */
    void insertUser(User user);

    /**
     * 更新用户
     * @param user
     * @return
     */
    boolean updateUser(User user);

    /**
     * 更改密码
     * @param password
     * @return
     */
    boolean updatePassword(String password);

    /**
     * 删除用户
     * @param user
     * @return
     */
    boolean deleteUser(User user);
}
