package com.chin.gameserver.interfaces.record;

import com.alibaba.fastjson.JSONObject;
import com.chin.gameserver.application.record.IRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/22 22:42
 */
@RestController
public class RecordController {

    @Resource
    private IRecordService recordService;

    @GetMapping("/record/getlist/")
    public JSONObject getRecordList(@RequestParam Map<String, String> data) {
        Integer page = Integer.valueOf(data.get("page"));
        return recordService.getRecordList(page);
    }
}
