package com.chin.gameoauth.infrastructure.common;

/**
 * @author qi
 * @description 常量
 */
public class Constants {

    public static final class JwtConstants {

        /**
         * 有效时间-14天
         */
        public static final long JWT_TTL = 60 * 60 * 1000L * 24 * 14;

        /**
         * 自定义秘钥
         */
        public static final String JWT_KEY = "SDFGjhdsfalshdfHFdsjkdsfds121232131afasdfac";

    }

}
