package com.chin.gamegateway.filter;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.chin.gamegateway.component.HandleException;
import com.chin.gamegateway.config.IgnoreUrlsConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qi
 * @description 全局Filter
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Resource
    private HandleException handler;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // white person
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean flag = false;
        String path = exchange.getRequest().getURI().getPath();

        // 1. 先查看白名单，如果是白名单直接往下走
        for (String url : ignoreUrlsConfig.getUrls()) {
            if (antPathMatcher.match(url, path)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            return chain.filter(exchange);
        }
        // 2. 获取query里面的access_token
        String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(accessToken) || !accessToken.startsWith("Bearer ")) {
            // no Authorization continue
            return handler.writeError(exchange, "access_token is empty");
        }

        accessToken = accessToken.substring(7);
        String uId = exchange.getRequest().getHeaders().getFirst("uId");
        if (StringUtils.isBlank(uId)) {
            return handler.writeError(exchange, "access_token is empty");
        }
        // 3. 去auth-service中检查token是否正确
        String checkTokenUrl = "http://game-auth/auth/check_token";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("uID", uId);
        paramMap.put("token", accessToken);
        try {
            String resultStr = HttpUtil.post(checkTokenUrl, paramMap, 350);
            HashMap<String, String> resultMap = JSON.parseObject(resultStr, HashMap.class);
            // token no use
            if (resultMap.get("code") == "-1") {
                return handler.writeError(exchange, resultMap.get("message"));
            }
        } catch (Exception e) {
            return handler.writeError(exchange,
                    "Token was not recognised, token:".concat(accessToken));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
