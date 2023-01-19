package dev.leonardpark.ffmpeg.controller;

import dev.leonardpark.ffmpeg.model.CommonResponseModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "ffMpeg")
@RestController
@RequestMapping("/api/ffMpeg")
@RequiredArgsConstructor
public class ffMpegController {
  @ApiOperation(value = "test")
  @RequestMapping("test")
  public ResponseEntity<CommonResponseModel> test() {
    return CommonResponseModel.successResponse(true);
  }


}
