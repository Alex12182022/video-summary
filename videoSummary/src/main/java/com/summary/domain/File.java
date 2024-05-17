package com.summary.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class File {

    private Long id;

    private String url;

    private String type;

    private String md5;

    private Date createTime;
}
