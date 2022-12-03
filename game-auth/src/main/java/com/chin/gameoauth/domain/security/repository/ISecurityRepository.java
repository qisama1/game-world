package com.chin.gameoauth.domain.security.repository;

import com.chin.gameoauth.domain.security.model.VO.UserDetail;

/**
 * @author qi
 */
public interface ISecurityRepository {

    /**
     * 根据用户名查询UserDetail
     * @param username
     * @return
     */
    UserDetail queryUserDetailByName(String username);

    /**
     * 根据Id查询用户
     * @param id
     * @return
     */
    UserDetail queryUserDetailById(Integer id);
}
