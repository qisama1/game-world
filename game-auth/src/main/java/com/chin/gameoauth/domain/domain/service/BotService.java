package com.chin.gameoauth.domain.domain.service;

import com.chin.gameoauth.application.IBotService;
import com.chin.gameoauth.domain.domain.model.VO.BotVO;
import com.chin.gameoauth.domain.domain.repository.IBotRepository;
import com.chin.gameoauth.infrastructure.pojo.Bot;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qi
 */
@Service
public class BotService implements IBotService {

    @Resource
    private IBotRepository botRepository;

    @Override
    public Map<String, String> insertBot(String Content, String title, String description) {
        botRepository.insertBot(Content, title, description);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("error_message", "success");
        return resultMap;
    }

    @Override
    public Map<String, String> deleteBot(Integer id) {
        BotVO botVO = botRepository.queryBotById(id);
        boolean success = botRepository.deleteBot(id, botVO.getUserId());
        Map<String, String> resultMap = new HashMap<>();
        if (success) {
            resultMap.put("error_message", "success");
        } else {
            resultMap.put("error_message", "删除失败");
        }
        return resultMap;
    }

    @Override
    public List<BotVO> queryBotList() {
        return botRepository.queryBotList();
    }

    @Override
    public Map<String, String> queryBotById(Integer id) {
        BotVO botVO = botRepository.queryBotById(id);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("error_message", "success");
        resultMap.put("botId", botVO.getId().toString());
        resultMap.put("userId", botVO.getUserId().toString());
        resultMap.put("content", botVO.getContent());
        resultMap.put("title", botVO.getTitle());
        resultMap.put("description", botVO.getDescription());
        return resultMap;
    }

    @Override
    public Map<String, String> updateBot(Integer id, String content, String title, String description) {
        boolean success = botRepository.updateBot(id, content, title, description);
        Map<String, String> resultMap = new HashMap<>();
        if (success) {
            resultMap.put("error_message", "success");
        } else {
            resultMap.put("error_message", "插入失败");
        }
        return resultMap;
    }
}
