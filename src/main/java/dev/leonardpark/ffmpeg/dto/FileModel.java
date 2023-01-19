package dev.leonardpark.ffmpeg.dto;

import dev.leonardpark.ffmpeg.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileModel {
  private String fileId;
  private String url;
  private String name;
  private Long size;
  private String extension;
  private Long createdAt;

  public static FileModel convertFromFileEntity(FileEntity file) {
    return new FileModel(
      file.getId(),
      String.format("/api/file/%s", file.getId()),
      file.getFileName(),
      file.getFileSize(),
      file.getExtension(),
      file.getCreatedAt().getTime()
    );
  }
}
