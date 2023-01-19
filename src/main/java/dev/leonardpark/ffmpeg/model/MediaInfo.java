package dev.leonardpark.ffmpeg.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaInfo {

  private Format format;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Format {
    @SerializedName("bit_rate")
    private String bitRate;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stream {
      @SerializedName("index")
      private int index;

      @SerializedName("codec_name")
      private String codecName;

      @SerializedName("codec_long_name")
      private String codecLongame;

      @SerializedName("profile")
      private String profile;
    }

    // ----------------------------------

    @SerializedName("streams")
    private List<Stream> streams;

    @SerializedName("format")
    private Format format;
  }
}
