# coding=utf-8
# 导入必要的软件包
import cv2
# 视频文件输入初始化
filename = r"D:\mcip\差分法2\autovideo.mp4"
capture = cv2.VideoCapture(filename)

# 视频文件输出参数设置
fps = capture.get(cv2.CAP_PROP_FPS)  # fps of video
size = (int(capture.get(cv2.CAP_PROP_FRAME_WIDTH)),
        int(capture.get(cv2.CAP_PROP_FRAME_HEIGHT)))

fourcc = cv2.VideoWriter_fourcc('M', 'P', '4', '2')
out1 = cv2.VideoWriter('test1_v2.avi', fourcc, fps, size)
out2 = cv2.VideoWriter('test1_op.avi', fourcc, fps, size)

# 初始化当前帧的前帧
lastFrame = None

# 遍历视频的每一帧
while capture.isOpened():

    # 读取下一帧
    (ret, frame) = capture.read()

    # 如果不能抓取到一帧，说明我们到了视频的结尾
    if not ret:
        break


    # 如果第一帧是None，对其进行初始化
    if lastFrame is None:
        lastFrame = frame
        continue

        #Calculate the difference between the current frame and the previous frame
    frameDelta = cv2.absdiff(lastFrame, frame)

    # 当前帧设置为下一帧的前帧
    lastFrame = frame.copy()

    # 结果转为灰度图
    thresh = cv2.cvtColor(frameDelta, cv2.COLOR_BGR2GRAY)

    # 图像二值化
    _, thresh = cv2.threshold(thresh, 30, 255, cv2.THRESH_BINARY)

    area = cv2.sumElems(thresh)[0] / 255

    # Set a threshold to filter non motion frames
    if(area > thresh.shape[0] * thresh.shape[1] * 0.05):

        # 显示当前帧
        # cv2.imshow("frame", frame)
        # cv2.imshow("frameDelta", frameDelta)
        # thresh = cv2.cvtColor(thresh, cv2.COLOR_GRAY2BGR)
        # cv2.imshow("thresh", thresh)

        # 保存视频
        out1.write(frame)
        # out2.write(thresh)

        # 如果q键被按下，跳出循环
        # if cv2.waitKey(200) & 0xFF == ord('q'):
        #     break

    # 清理资源并关闭打开的窗口
out1.release()
out2.release()
capture.release()
cv2.destroyAllWindows()
