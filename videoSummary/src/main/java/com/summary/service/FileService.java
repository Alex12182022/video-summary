package com.summary.service;

import com.summary.dao.FileDao;
import com.summary.domain.File;
import com.summary.util.FastDFSUtil;
import com.summary.util.MD5Util;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class FileService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    public String uploadFileBySlices(MultipartFile slice,
                                     String fileMD5,
                                     Integer sliceNo,
                                     Integer totalSliceNo) throws Exception {
        File dbFileMD5 = fileDao.getFileByMD5(fileMD5);
        if(dbFileMD5 != null){
            return dbFileMD5.getUrl();
        }
        String url = fastDFSUtil.uploadFileBySlices(slice, fileMD5, sliceNo, totalSliceNo);
        if(!StringUtil.isNullOrEmpty(url)){
            dbFileMD5 = new File();
            dbFileMD5.setCreateTime(new Date());
            dbFileMD5.setMd5(fileMD5);
            dbFileMD5.setUrl(url);
            dbFileMD5.setType(fastDFSUtil.getFileType(slice));
            fileDao.addFile(dbFileMD5);
        }
        return url;
    }

    public String getFileMD5(MultipartFile file) throws Exception {
        return MD5Util.getFileMD5(file);
    }

    public File getFileByMd5(String fileMd5) {
        return fileDao.getFileByMD5(fileMd5);
    }


}
