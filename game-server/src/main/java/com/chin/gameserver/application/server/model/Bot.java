package com.chin.gameserver.application.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/19 20:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {

    private Integer id;

    private Integer userId;

    private String content;

    private String description;

    private String title;
}
