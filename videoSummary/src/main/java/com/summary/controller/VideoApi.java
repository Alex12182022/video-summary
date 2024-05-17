package com.summary.controller;

import com.summary.controller.support.UserSupport;
import com.summary.domain.JsonResponse;
import com.summary.domain.Video;
import com.summary.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class VideoApi {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserSupport userSupport;

    /**
     * 视频投稿
     */
    @PostMapping("/videos")
    public JsonResponse<String> addVideos(@RequestBody Video video){
//        Long userId = userSupport.getCurrentUserId();
//        video.setUserId(userId);
        videoService.addVideos(video);
        return JsonResponse.success();
    }

    /**
     * 视频在线播放
     */
    @GetMapping("/video-slices")
    public void viewVideoOnlineBySlices(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String url) {
        videoService.viewVideoOnlineBySlices(request, response, url);
    }

    /**
     * 视频帧截取生成黑白剪影
     */
    @GetMapping("/video-frames")
    public JsonResponse<String> captureVideoFrame(@RequestParam Long videoId,
                                                                    @RequestParam String fileMd5) throws Exception {
        String url = videoService.convertVideoToImage(videoId, fileMd5);
        return new JsonResponse<>(url);
    }


}
