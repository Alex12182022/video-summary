package com.summary.dao;

import com.summary.domain.Video;
import com.summary.domain.VideoBinaryPicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoDao {

    Integer addVideos(Video video);

    Integer batchAddVideoBinaryPictures(@Param("pictureList") List<VideoBinaryPicture> pictureList);

    List<VideoBinaryPicture> getVideoBinaryImages(Map<String, Object> params);
}
