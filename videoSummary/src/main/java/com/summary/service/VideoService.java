package com.summary.service;

import com.summary.dao.VideoDao;
import com.summary.domain.Video;
import com.summary.domain.exception.ConditionException;
import com.summary.util.FastDFSUtil;
import lombok.extern.log4j.Log4j2;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;


@Service
@Log4j2
public class VideoService {

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private UserService userService;


    @Autowired
    private FileService fileService;

    private static final int FRAME_NO = 128;

    @Transactional
    public void addVideos(Video video) {
        video.setCreateTime(new Date());
        videoDao.addVideos(video);
    }

    public void viewVideoOnlineBySlices(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String url) {
        try{
            fastDFSUtil.viewVideoOnlineBySlices(request, response, url);
        }catch (Exception ignored){}
    }


    public String convertVideoToImage(Long videoId, String fileMd5) throws Exception{
        com.summary.domain.File file = fileService.getFileByMd5(fileMd5);
        String filePath = "/Users/zhanggongbo/Documents/home/dfs/tmpfile/fileForVideoId" + videoId + "." + file.getType();
        String frameStorePath = "/Users/zhanggongbo/Documents/home/dfs/tmpfile/tmpFrame";
        log.info("filePath:{}",filePath);
        fastDFSUtil.downLoadFile(file.getUrl(), filePath);

        //FFmpegFrameGrabber fFmpegFrameGrabber1 = FFmpegFrameGrabber.createDefault(filePath);
        FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(filePath);
        fFmpegFrameGrabber.start();
        //视频总帧数
        int ffLength = fFmpegFrameGrabber.getLengthInFrames();
        log.info("ffleng: {}",ffLength);
        Frame frame;
        int count = 1;
        Java2DFrameConverter converter = new Java2DFrameConverter();
        //生成所有的帧的保存，结果
        for(int i=0; i< ffLength; i ++){
            //这一帧的时间戳
            //变成图片
            frame = fFmpegFrameGrabber.grabImage();
            //如果帧数很大，可以按照一定频率截取，增加速度
            if(count == i){
                if(frame == null){
                    throw new ConditionException("无效帧");
                }
                log.info("count:{}",i);
                String fileName = frameStorePath + "/img_"+count+".jpg";
                log.info("filename: {}",fileName);
                java.io.File outPutFile = new File(fileName);

                ImageIO.write(converter.getBufferedImage(frame),"jpg",outPutFile);
                count += FRAME_NO;//每隔这个频率截取一帧
            }
        }

        File files = new File(frameStorePath);
        File[] filess = files.listFiles();
        Map<Integer, File> imgMap = new HashMap<Integer, File>();
        int num = 0;
        for (File imgFile : filess) {
            imgMap.put(num, imgFile);
            num++;
        }
        createMp4(frameStorePath+"iml.mp4",imgMap,1600,900);

        String newUrl = fastDFSUtil.uploadCommonFile(new File(frameStorePath+"iml.mp4"),"mp4");
        //删除临时文件
        File tmpFile = new File(filePath);
        //File tmpFile1 = new File(frameStorePath);
        tmpFile.delete();
        //tmpFile1.delete();
        log.info("summary successfully");
        return newUrl;
    }


    private static void createMp4(String mp4SavePath, Map<Integer, File> imgMap, int width, int height) throws FrameRecorder.Exception {
        //视频宽高最好是按照常见的视频的宽高  16：9  或者 9：16
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(mp4SavePath, width, height);
        //设置视频编码层模式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        //设置视频为25帧每秒
        recorder.setFrameRate(3);
        //设置视频图像数据格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setFormat("mp4");
        try {
            recorder.start();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            //录制一个22秒的视频

            for (int i = 0; i < imgMap.size()/3; i++) {
                BufferedImage read = ImageIO.read(imgMap.get(i));
                //一秒是25帧 所以要记录25次
                //for (int j = 0; j < 3; j++) {
                    recorder.record(converter.getFrame(read));
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最后一定要结束并释放资源
            recorder.stop();
            recorder.release();
            log.info("success");
        }
    }


}
