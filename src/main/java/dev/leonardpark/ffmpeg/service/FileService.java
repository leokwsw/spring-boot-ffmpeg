package dev.leonardpark.ffmpeg.service;

import dev.leonardpark.ffmpeg.model.FileModel;
import dev.leonardpark.ffmpeg.model.GetFileListQueryModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
  List<FileModel> getList(GetFileListQueryModel queryModel);
  FileModel uploadFile(MultipartFile file) throws IOException;
  String getFileFullPath(String fileId);
}
