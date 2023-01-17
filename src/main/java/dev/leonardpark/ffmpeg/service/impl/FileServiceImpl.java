package dev.leonardpark.ffmpeg.service.impl;

import dev.leonardpark.ffmpeg.model.FileModel;
import dev.leonardpark.ffmpeg.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {
  @Value("${ffmpeg.dir:''}")
  private String dir;


  @Override
  public FileModel uploadFile(MultipartFile file) throws IOException {
    return null;
  }
}
