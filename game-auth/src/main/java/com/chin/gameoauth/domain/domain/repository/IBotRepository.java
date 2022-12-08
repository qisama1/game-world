package com.chin.gameoauth.domain.domain.repository;

import com.chin.gameoauth.domain.domain.model.VO.BotVO;

import java.util.List;

/**
 * @author qi
 */
public interface IBotRepository {

    /**
     * 查询Bot列表
     * @return
     */
    List<BotVO> queryBotList();

    /**
     * 查询bot信息
     * @param id
     * @return
     */
    BotVO queryBotById(Integer id);

    /**
     * 插入Bot
     * @param content
     * @param title
     * @param description
     */
    void insertBot(String content, String title, String description);

    /**
     * 更新Bot
     * @param id
     * @param content
     * @param title
     * @param description
     * @return
     */
    boolean updateBot(Integer id, String content, String title, String description);

    /**
     * 删除Bot
     * @param id
     * @param userId
     * @return
     */
    boolean deleteBot(Integer id, Integer userId);
}
