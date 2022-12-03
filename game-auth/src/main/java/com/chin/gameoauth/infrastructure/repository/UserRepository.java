package com.chin.gameoauth.infrastructure.repository;

import com.chin.gameoauth.domain.user.model.UserVO;
import com.chin.gameoauth.domain.user.repository.IUserRepository;
import com.chin.gameoauth.infrastructure.dao.IUserDao;
import com.chin.gameoauth.infrastructure.pojo.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qi
 */
@Component
public class UserRepository implements IUserRepository {

    @Resource
    private IUserDao userDao;

    @Override
    public UserVO queryUserByName(String username) {
        if (null == username || "".equals(username)) {
            return null;
        }
        User user = userDao.queryUserByName(username);
        UserVO userVO = new UserVO(user.getId(), user.getUsername(), user.getPhoto(), user.getRating());
        return userVO;
    }

    @Override
    public UserVO queryUserById(Integer uId) {
        if (null == uId) {
            return null;
        }
        User user = userDao.queryUserById(uId);
        UserVO userVO = new UserVO(user.getId(), user.getUsername(), user.getPhoto(), user.getRating());
        return userVO;
    }


    @Override
    public void insertUser(String username, String password, String photo, Integer rating) {
        User user = new User(username, password, photo, rating);
        userDao.insertUser(user);
    }

    @Override
    public boolean updateUser(String username, String photo, Integer rating) {
        User user = new User();
        user.setUsername(username);
        user.setPhoto(photo);
        user.setRating(rating);
        return userDao.updateUser(user);
    }

    @Override
    public boolean updatePassword(String password) {
        return userDao.updatePassword(password);
    }

    @Override
    public boolean deleteUser(Integer uId) {
        User user = new User();
        user.setId(uId);
        return userDao.deleteUser(user);
    }


}
