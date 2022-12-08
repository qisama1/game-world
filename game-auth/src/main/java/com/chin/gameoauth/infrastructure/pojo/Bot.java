package com.chin.gameoauth.infrastructure.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author qi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bot {

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

    /**
     * 创建的时间
     */
    Date createTime;

    /**
     * 修改的时间
     */
    Date modifiedTime;
}
