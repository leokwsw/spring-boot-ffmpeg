package dev.leonardpark.ffmpeg.service.impl;

import dev.leonardpark.ffmpeg.model.MediaInfo;
import dev.leonardpark.ffmpeg.service.ffMpegService;
import org.springframework.stereotype.Service;

@Service
public class ffMpegServiceImpl implements ffMpegService {
  @Override
  public String convertMp4ToM3u8(String mp4FilePath, String targetDir) {
    return null;
  }

  @Override
  public MediaInfo getVideoInfo(String targetVideoPath) {
    return null;
  }

  @Override
  public String screenshot(String videoPath, String time, String targetFile) {
    return null;
  }
}
