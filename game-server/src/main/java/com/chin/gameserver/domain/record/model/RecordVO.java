package com.chin.gameserver.domain.record.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/22 22:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVO {

    private Integer id;

    private Integer aId;

    private Integer aSx;

    private Integer aSy;

    private String aSteps;

    private Integer bId;

    private Integer bSx;

    private Integer bSy;

    private String bSteps;

    private String map;

    private String loser;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createTime;

}
