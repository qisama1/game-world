package com.chin.gameserver.infrastrcture.repository;

import com.chin.gameserver.domain.record.model.RecordVO;
import com.chin.gameserver.domain.record.repository.IRecordRepository;
import com.chin.gameserver.infrastrcture.dao.RecordDao;
import com.chin.gameserver.infrastrcture.pojo.Record;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/22 22:27
 */
@Component
public class RecordRepository implements IRecordRepository {

    @Resource
    private RecordDao recordDao;

    @Override
    public List<RecordVO> getRecordList(Integer page) {
        List<Record> records = recordDao.getRecordList((page - 1) * 10);
        List<RecordVO> recordVOS = new ArrayList<>();
        for (Record record : records) {
            RecordVO recordVO = new RecordVO(record.getId(), record.getAId(), record.getASx(), record.getASy(), record.getASteps(),
                    record.getBId(), record.getBSx(), record.getBSy(), record.getBSteps(), record.getMap(), record.getLoser(), record.getCreateTime());
            recordVOS.add(recordVO);
        }
        return recordVOS;
    }

    @Override
    public void insertRecord(Integer aId, Integer aSx, Integer aSy,
                             String aSteps, Integer bId, Integer bSx, Integer bSy,
                             String bStep, String map, String loser) {
        Record record = new Record(null, aId, aSx, aSy, aSteps, bId, bSx, bSy, bStep, map, loser, null);
        recordDao.insertRecord(record);
    }

    @Override
    public Integer getRecordCount() {
        return recordDao.getRecordCount();
    }
}
