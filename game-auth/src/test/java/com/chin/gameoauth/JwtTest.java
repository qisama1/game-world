package com.chin.gameoauth;

import com.chin.gameoauth.domain.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

public class JwtTest {

    @Test
    public void testJWT() throws Exception {
        String jwt = JwtUtil.createJWT("ABC");
        Claims claims = JwtUtil.parseJWT(jwt);
        System.out.println(claims);
    }
}
