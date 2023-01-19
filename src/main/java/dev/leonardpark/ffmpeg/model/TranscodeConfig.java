package dev.leonardpark.ffmpeg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscodeConfig {
  private String poster;              // 截取封面的時間          HH:mm:ss.[SSS]
  private String tsSeconds;           // ts分片大小，單位是秒
  private String cutStart;            // 視頻裁剪，開始時間        HH:mm:ss.[SSS]
  private String cutEnd;              // 視頻裁剪，結束時間        HH:mm:ss.[SSS]

  @Override
  public String toString() {
    return "TranscodeConfig [poster=" + poster + ", tsSeconds=" + tsSeconds + ", cutStart=" + cutStart + ", cutEnd="
      + cutEnd + "]";
  }
}
