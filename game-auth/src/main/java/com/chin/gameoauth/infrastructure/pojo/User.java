package com.chin.gameoauth.infrastructure.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qi
 * @description 用户pojo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;

    private String username;

    private String password;

    private String photo;

    private Integer rating;

    public User(String username, String password, String photo, Integer rating) {
        this.username = username;
        this.password = password;
        this.photo = photo;
        this.rating = rating;
    }

}
