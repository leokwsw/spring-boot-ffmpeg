package dev.leonardpark.ffmpeg.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFileListQueryModel {
  @ApiModelProperty(value = "skip")
  private Integer skip = 0;
  @ApiModelProperty(value = "limit")
  private Integer limit = 10;
  @ApiModelProperty(value = "keyword")
  private String keyword;
  @ApiModelProperty(value = "extension")
  private String extension;
  @ApiModelProperty(value = "get all")
  private boolean isGetAll = false;
}
