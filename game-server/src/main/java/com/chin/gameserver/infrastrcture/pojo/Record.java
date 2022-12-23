package com.chin.gameserver.infrastrcture.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/22 22:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {

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

    private Date createTime;

}
