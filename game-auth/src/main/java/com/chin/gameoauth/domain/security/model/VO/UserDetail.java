package com.chin.gameoauth.domain.security.model.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {

    private Integer id;

    private String username;

    private String password;

    private String photo;

    private Integer rating;

}
