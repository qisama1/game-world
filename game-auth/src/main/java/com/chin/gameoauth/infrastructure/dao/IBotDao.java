package com.chin.gameoauth.infrastructure.dao;

import com.chin.gameoauth.infrastructure.pojo.Bot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author qi
 */
@Mapper
public interface IBotDao {

    /**
     * 根据用户id查询bot
     * @param userId
     * @return
     */
    List<Bot> queryBotByUserId(Integer userId);

    /**
     * 插入bot
     * @param bot
     */
    void insertBot(Bot bot);

    /**
     * 根据bot的id查出具体内容
     * @param id
     * @return
     */
    Bot queryBotById(Integer id);

    /**
     * 删除bot
     * @param bot
     * @return
     */
    boolean deleteBot(Bot bot);

    /**
     * 修改Bot
     * @param bot
     * @return
     */
    boolean updateBot(Bot bot);
}
