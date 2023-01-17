package dev.leonardpark.ffmpeg.service;

import dev.leonardpark.ffmpeg.model.FileModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
  FileModel uploadFile(MultipartFile file) throws IOException;
  String getFileFullPath(String fileId);
}
