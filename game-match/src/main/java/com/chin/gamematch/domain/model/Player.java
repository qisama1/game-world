package com.chin.gamematch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/15 18:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    private Integer userId;
    private Integer rating;
    private Integer waitingTime;
    private Integer botId;
    private boolean isQuited;

}
