package com.chin.gameoauth.interfaces.controller;

import com.chin.gameoauth.application.IBotService;
import com.chin.gameoauth.domain.domain.model.VO.BotVO;
import com.chin.gameoauth.infrastructure.pojo.Bot;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qi
 */
@RestController
public class BotController {

    @Resource
    IBotService botService;

    @GetMapping("/user/bot/getlist")
    public List<BotVO> queryBotList() {
        // 设计思路，因为当前用于已经通过了SpringSecurity的认证，所以它的内容已经可以在SecurityContextHolder里面找到
        // 如下：SecurityContextHolder.getContext().getAuthentication()，所以可以这样获取用户的信息，保证安全性。
        return botService.queryBotList();
    }

    @PostMapping("/user/bot/remove/")
    public Map<String, String> deleteBot(@RequestParam Map<String, String> map) {
        String id = map.get("bot_id");
        return botService.deleteBot(Integer.valueOf(id));
    }

    @PostMapping("/user/bot/add/")
    public Map<String, String> insertBot(@RequestParam Map<String, String> map) {
        String title = map.get("title");
        String description = map.get("description");
        String content = map.get("content");
        return botService.insertBot(content, title, description);
    }

    @PostMapping("/user/bot/update/")
    public Map<String, String> updateBot(@RequestParam Map<String, String> map) {
        Integer id = Integer.valueOf(map.get("bot_id"));
        String title = map.get("title");
        String description = map.get("description");
        String content = map.get("content");
        return botService.updateBot(id, content, title, description);
    }

}
