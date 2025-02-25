import cv2
import os
import numpy as np
from PIL import Image
import shutil
import cut


def frame2video(im_dir, video_dir, fps):
    im_list = os.listdir(im_dir)
    im_list.sort(key=lambda x: int(x.replace("frame", "").split('.')[0]))  # 最好再看看图片顺序对不
    img = Image.open(os.path.join(im_dir, im_list[0]))
    img_size = img.size  # 获得图片分辨率，im_dir文件夹下的图片分辨率需要一致

    # fourcc = cv2.cv.CV_FOURCC('M','J','P','G') #opencv版本是2
    fourcc = cv2.VideoWriter_fourcc(*'XVID')  # opencv版本是3
    videoWriter = cv2.VideoWriter(video_dir, fourcc, fps, img_size)
    # count = 1
    for i in im_list:
        im_name = os.path.join(im_dir + i)
        frame = cv2.imdecode(np.fromfile(im_name, dtype=np.uint8), -1)
        videoWriter.write(frame)
        # count+=1
        # if (count == 200):
        #     print(im_name)
        #     break
    videoWriter.release()
    print('finish')


if __name__ == '__main__':
    videos_path = r"D:\mcip\cutandMerge\autovideo.mp4"
    frames_save_path = r"D:\mcip\cutandMerge\testfile1"
    shutil.rmtree(frames_save_path)#删除文件夹内容
    os.mkdir(frames_save_path)
    time_interval = 2  # 隔一帧保存一次#不能用3的倍数
    cut.video2frame(videos_path, frames_save_path, time_interval)
    im_dir = 'D:/mcip/cutandMerge/testfile1/'  # 帧存放路径
    video_dir = r"D:\mcip\cutandMerge\test.mp4"  # 合成视频存放的路径
    fps = 30  # 帧率，每秒钟帧数越多，所显示的动作就会越流畅
    frame2video(im_dir, video_dir, fps)