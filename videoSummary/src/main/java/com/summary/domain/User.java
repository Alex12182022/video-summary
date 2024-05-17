package com.summary.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {

    private Long id;

    private String phone;

    private String email;

    private String password;

    private String salt;

    private Date createTime;

    private Date updateTime;

}
