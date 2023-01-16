package dev.leonardpark.ffmpeg.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseModel {
  @ApiModelProperty(value = "success value")
  private Boolean success;

  public static ResponseEntity<CommonResponseModel> successResponse(boolean success) {
    return ResponseEntity.ok(new CommonResponseModel(success));
  }
}
