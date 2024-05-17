package com.summary.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Video {

    private Long id;

    private Long userId;//用户id

    private String url; //视频链接

    private Date createTime;


}
