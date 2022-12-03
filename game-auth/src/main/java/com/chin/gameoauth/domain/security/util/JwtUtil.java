package com.chin.gameoauth.domain.security.util;

import com.chin.gameoauth.infrastructure.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author qi
 */
public class JwtUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成JWT-token
     * @param subject
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    /**
     * 获取JWT builder
     * @param subject
     * @param ttlMills
     * @param uuid
     * @return
     */
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMills, String uuid) {
        // 1. 指定签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 2. 生成秘钥
        SecretKey secretKey = generalKey();
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        if (ttlMills == null) {
            ttlMills = Constants.JwtConstants.JWT_TTL;
        }
        long expMills = ttlMills + nowMills;
        Date expDate = new Date(expMills);
        return Jwts.builder().setId(uuid).setSubject(subject)
                .setIssuer("chin").setIssuedAt(now).signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    /**
     * 生成秘钥
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(Constants.JwtConstants.JWT_KEY);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "HmacSHA256");
    }

    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
