package com.chin.gameoauth.infrastructure.repository;

import com.chin.gameoauth.domain.security.model.VO.UserDetail;
import com.chin.gameoauth.domain.security.repository.ISecurityRepository;
import com.chin.gameoauth.infrastructure.dao.IUserDao;
import com.chin.gameoauth.infrastructure.pojo.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qi
 */
@Component
public class SecurityRepository implements ISecurityRepository {

    @Resource
    private IUserDao userDao;

    @Override
    public UserDetail queryUserDetailByName(String username) {
        User user = userDao.queryUserByName(username);
        UserDetail userDetail = new UserDetail(user.getId(), user.getUsername(), user.getPassword(), user.getPhoto(), user.getRating());
        return userDetail;
    }

    @Override
    public UserDetail queryUserDetailById(Integer id) {
        User user = userDao.queryUserById(id);
        UserDetail userDetail = new UserDetail(user.getId(), user.getUsername(), user.getPassword(), user.getPhoto(), user.getRating());
        return userDetail;
    }
}
