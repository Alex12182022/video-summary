package com.summary.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VideoBinaryPicture {

    private Long id;

    private Long videoId;

    private Integer frameNo;

    private String url;

    private Long videoTimestamp;

    private Date createTime;


}
