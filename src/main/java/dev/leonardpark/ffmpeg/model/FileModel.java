package dev.leonardpark.ffmpeg.model;

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
}
