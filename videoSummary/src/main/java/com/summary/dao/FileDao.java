package com.summary.dao;

import com.summary.domain.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDao {

    Integer addFile(File file);

    File getFileByMD5(String md5);

}
