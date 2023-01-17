package dev.leonardpark.ffmpeg.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("file")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class File extends Base {
  private String fileName;
  private String fullPath;
  private Long fileSize;
  private String extension;
  private String md5;
}
