package com.chin.gameserver.domain.record.service;

import com.alibaba.fastjson.JSONObject;
import com.chin.gameserver.application.record.IRecordService;
import com.chin.gameserver.application.server.handlers.SocketHandler;
import com.chin.gameserver.application.server.model.Bot;
import com.chin.gameserver.domain.record.model.RecordVO;
import com.chin.gameserver.domain.record.model.UserVO;
import com.chin.gameserver.domain.record.repository.IRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/22 22:23
 */
@Service
public class RecordService implements IRecordService {

    @Resource
    private IRecordRepository recordRepository;

    @Override
    public JSONObject getRecordList(Integer page) {
        List<RecordVO> records = recordRepository.getRecordList(page);
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for (RecordVO record : records) {
            UserVO userA = getUserById(record.getAId());
            UserVO userB = getUserById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            String result = "draw";
            if ("A".equals(record.getLoser())) {
                result = "Bwin";
            } else if ("B".equals(record.getLoser())) {
                result = "Awin";
            }
            item.put("result", result);
            item.put("record", record);
            items.add(item);
        }

        resp.put("records", items);
        resp.put("records_count", recordRepository.getRecordCount());
        return resp;
    }

    private UserVO getUserById(Integer userId) {
        UserVO userVO = new UserVO();
        MultiValueMap<String, Integer> data = new LinkedMultiValueMap<>();
        String queryByIdUrl = "http://game-auth/user/account/queryById";
        data.add("userId", userId);
        HashMap<String, String> map = SocketHandler.restTemplate.postForObject(queryByIdUrl, data, HashMap.class);
        String username = map.get("username");
        String photo = map.get("photo");
        userVO.setUsername(username);
        userVO.setPhoto(photo);
        return userVO;
    }


    @Override
    public void insertRecord(Integer aId, Integer aSx, Integer aSy,
                             String aSteps, Integer bId, Integer bSx,
                             Integer bSy, String bStep, String map, String loser) {
        recordRepository.insertRecord(aId, aSx, aSy, aSteps, bId, bSx, bSy, bStep, map, loser);
    }

    @Override
    public Integer getRecordCount() {
        return recordRepository.getRecordCount();
    }

}
