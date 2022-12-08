package com.chin.gameoauth.application;

import com.chin.gameoauth.domain.domain.model.VO.BotVO;

import java.util.List;
import java.util.Map;

/**
 * @author qi
 */
public interface IBotService {

    /**
     * 插入Bot
     * @param Content
     * @param title
     * @param description
     * @return
     */
    Map<String, String> insertBot(String Content, String title, String description);

    /**
     * 删除Bot
     * @param id
     * @return
     */
    Map<String, String> deleteBot(Integer id);

    /**
     * 查询user的所有Bot
     * @return
     */
    List<BotVO> queryBotList();

    /**
     * 根据Id查询bot内容
     * @param id
     * @return
     */
    Map<String, String> queryBotById(Integer id);

    /**
     * 更新Bot
     * @param id
     * @param content
     * @param title
     * @param description
     * @return
     */
    Map<String, String> updateBot(Integer id, String content, String title, String description);
}
