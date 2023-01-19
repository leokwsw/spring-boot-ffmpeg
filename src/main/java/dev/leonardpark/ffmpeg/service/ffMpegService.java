package dev.leonardpark.ffmpeg.service;

import dev.leonardpark.ffmpeg.model.MediaInfo;

public interface ffMpegService {
  // mp4 to m3u8
  String convertMp4ToM3u8(String mp4FilePath, String targetDir);
  // get video info
  MediaInfo getVideoInfo(String targetVideoPath);
  // screenshot
  String screenshot(String videoPath, String time, String targetFile);
}
