package com.summary.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RefreshTokenDetail {

    private Long id;

    private String refreshToken;

    private Long userId;

    private Date createTime;
}
