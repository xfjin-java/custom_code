package com.exfjin.tool;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class VideoClippingTool {
    //FFmpeg全路径
    private static final String FFMPEG_PATH = "D:\\install_path\\ffmpeg\\bin\\ffmpeg.exe";

    public static void main(String[] args) {
        String videoInputPath = "D:\\合成\\video.mp4";
        String audioInputPath = "D:\\合成\\遇见.mp3";
        String videoOutPath = "D:\\合成\\res\\res.mp4";
        convetor(videoInputPath,audioInputPath,videoOutPath);
    }


    public static void convetor(String videoInputPath, String audioInputPath, String videoOutPath) {
        Process process = null;
        try {
            log.error("开始合成视频,音频");
            String command = FFMPEG_PATH + " -i " + videoInputPath + " -i " + audioInputPath + " -c:v copy -c:a aac -strict experimental " +
                    " -map 0:v:0 -map 1:a:0 "
                    + " -y " + videoOutPath;
            process = Runtime.getRuntime().exec(command);
            //消费正常日志
            clearStream(process.getInputStream());
            //消费错误日
            clearStream(process.getErrorStream());
            int res = process.waitFor();
            if(res != 0){
                log.error("视频,音频合成失败!");
            }
            log.info("视频,音频合成成功!");
        } catch (Exception e) {
            log.error(e.getMessage());
        }finally {
            if (process != null) {
                process.destroy();
            }
        }

    }


    public static void clearStream(InputStream stream) {
        ThreadUtil.execAsync(new Runnable() {
            @Override
            public void run() {
                InputStreamReader reader = null;
                BufferedReader br = null;
                try {
                    reader = new InputStreamReader(stream);
                    br = new BufferedReader(reader);
                    String message;
                    while ((message = br.readLine()) != null) {
                        log.info(message);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (br != null) {
                            br.close();
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        });
    }
}
