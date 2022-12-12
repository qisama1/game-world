package com.chin.gameserver.application.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;

    private String username;

    private String photo;

    private Integer rating;

    public User(String username, String photo, Integer rating) {
        this.username = username;
        this.photo = photo;
        this.rating = rating;
    }

}