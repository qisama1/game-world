package com.chin.gamebot.domain.bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/29 12:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotVO {

    Integer userId;
    String botCode;
    String input;

}
