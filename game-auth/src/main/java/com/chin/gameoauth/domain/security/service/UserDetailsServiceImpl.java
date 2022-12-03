package com.chin.gameoauth.domain.security.service;

import com.chin.gameoauth.domain.security.model.UserDetailsImpl;
import com.chin.gameoauth.domain.security.model.VO.UserDetail;
import com.chin.gameoauth.domain.security.repository.ISecurityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qi
 * @description Security的配置，在这里查询用户
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private ISecurityRepository securityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail userDetail = securityRepository.queryUserDetailByName(username);
        if (null == userDetail) {
            throw new RuntimeException("用户不存在");
        }
        return new UserDetailsImpl(userDetail);
    }
}
