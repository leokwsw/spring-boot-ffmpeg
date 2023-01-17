package dev.leonardpark.ffmpeg.service.impl;

import dev.leonardpark.ffmpeg.entity.FileEntity;
import dev.leonardpark.ffmpeg.exception.FileNotFountException;
import dev.leonardpark.ffmpeg.model.FileModel;
import dev.leonardpark.ffmpeg.respository.FileRepository;
import dev.leonardpark.ffmpeg.service.FileService;
import dev.leonardpark.ffmpeg.utils.FileUtil;
import dev.leonardpark.ffmpeg.utils.MD5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

  @Autowired
  private FileRepository fileRepository;

  @Value("${ffmpeg.dir:''}")
  private File dir;


  @Override
  public FileModel uploadFile(MultipartFile file) throws IOException {
    String path = dir.getAbsolutePath();

    String fileName = file.getOriginalFilename();
    String filePath = String.format("%s/%s", path, fileName);

    File dest = new File(filePath);

    while (dest.exists()) {
      fileName = String.format("%s_%s", new Date().getTime(), file.getOriginalFilename());
      filePath = String.format("%s/%s", path, fileName);
      dest = new File(filePath);
    }

    file.transferTo(dest);

    FileEntity uploadFile = new FileEntity();

    uploadFile.setFileName(fileName);
    uploadFile.setFullPath(filePath);
    uploadFile.setFileSize(file.getSize());
    uploadFile.setExtension(FileUtil.getPathExt(filePath));
    uploadFile.setMd5(MD5Hash.getMD5Hash(dest));

    uploadFile = fileRepository.save(uploadFile);

    return FileModel.convertFromFileEntity(uploadFile);
  }

  @Override
  public String getFileFullPath(String fileId) {
    Optional<FileEntity> file = fileRepository.findById(fileId);
    if (file.isEmpty()) {
      throw new FileNotFountException(String.format("File Id %s if not fount", fileId));
    }
    FileEntity fileEntity = file.get();
    if (!new File(fileEntity.getFullPath()).exists()) {
      throw new FileNotFountException(String.format("File Id %s if not fount", fileId));
    }
    return fileEntity.getFullPath();
  }
}
