package com.chin.gameoauth.domain.security.filter;


import com.chin.gameoauth.domain.security.model.UserDetailsImpl;
import com.chin.gameoauth.domain.security.model.VO.UserDetail;
import com.chin.gameoauth.domain.security.repository.ISecurityRepository;
import com.chin.gameoauth.domain.security.util.JwtUtil;
import com.chin.gameoauth.domain.user.model.UserVO;
import com.chin.gameoauth.domain.user.repository.IUserRepository;
import com.sun.istack.internal.NotNull;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Resource
    private ISecurityRepository securityRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            // no Authorization continue
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7);

        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserDetail userDetail = securityRepository.queryUserDetailById(Integer.parseInt(userid));

        if (userDetail == null) {
            throw new RuntimeException("用户名未登录");
        }

        UserDetailsImpl loginUser = new UserDetailsImpl(userDetail);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
