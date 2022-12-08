package com.chin.gameoauth.domain.domain.model.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qi
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BotVO {

    /**
     * 主键
     */
    Integer id;

    /**
     * 用户Id
     */
    Integer userId;

    /**
     * Bot的执行代码
     */
    String content;

    /**
     * Bot的描述
     */
    String description;

    /**
     * Bot的标题
     */
    String title;

}
