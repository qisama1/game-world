package com.chin.gameserver.infrastrcture.dao;

import com.alibaba.fastjson.JSONObject;
import com.chin.gameserver.infrastrcture.pojo.Record;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/22 22:27
 */
@Mapper
public interface RecordDao {

    /**
     * 获取对应页的Record列表
     * @param offset
     * @return
     */
    List<Record> getRecordList(Integer offset);

    /**
     * 插入记录
     * @param record
     */
    void insertRecord(Record record);

    /**
     * 得到所有的记录
     * @return
     */
    Integer getRecordCount();
}
