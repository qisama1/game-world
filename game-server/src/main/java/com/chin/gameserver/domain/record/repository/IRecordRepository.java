package com.chin.gameserver.domain.record.repository;

import com.alibaba.fastjson.JSONObject;
import com.chin.gameserver.domain.record.model.RecordVO;

import java.util.List;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/22 22:23
 */
public interface IRecordRepository {

    /**
     * 获取对应页的记录
     * @param page
     * @return
     */
    List<RecordVO> getRecordList(Integer page);

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
     * 查询记录总数
     * @return
     */
    Integer getRecordCount();
}
