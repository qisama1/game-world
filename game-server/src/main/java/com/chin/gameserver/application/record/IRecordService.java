package com.chin.gameserver.application.record;

import com.alibaba.fastjson.JSONObject;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/22 21:28
 */
public interface IRecordService {

    /**
     * 获取所有的对局记录
     * @param page
     * @return
     */
    JSONObject getRecordList(Integer page);

    /**
     * 插入记录
     * @param aId
     * @param aSx
     * @param aSy
     * @param aSteps
     * @param bId
     * @param bSx
     * @param bSy
     * @param bStep
     * @param map
     * @param loser
     */
    void insertRecord(Integer aId, Integer aSx, Integer aSy, String aSteps, Integer bId, Integer bSx, Integer bSy,
                      String bStep, String map, String loser);

    /**
     * 获取总共的Record数目
     * @return
     */
    Integer getRecordCount();
}
