package com.chin.gameserver;

import com.chin.gameserver.infrastrcture.dao.RecordDao;
import com.chin.gameserver.infrastrcture.pojo.Record;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/23 11:01
 */
public class ApiTest {

    @Resource
    private RecordDao recordDao;

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void testRecord() {
        List<Record> recordList = recordDao.getRecordList(1);
        logger.info("{}", recordList);
    }
}
