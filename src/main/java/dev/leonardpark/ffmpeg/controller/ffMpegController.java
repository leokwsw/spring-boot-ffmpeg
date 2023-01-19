package dev.leonardpark.ffmpeg.controller;

import dev.leonardpark.ffmpeg.model.TranscodeConfig;
import dev.leonardpark.ffmpeg.utils.FFmpegUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "ffMpeg")
@RestController
@RequestMapping("/api/ffMpeg")
@RequiredArgsConstructor
public class ffMpegController {
//  @ApiOperation(value = "test")
//  @RequestMapping("test")
//  public ResponseEntity<CommonResponseModel> test() {
//    return CommonResponseModel.successResponse(true);
//  }

  private static final Logger logger = LoggerFactory.getLogger(ffMpegController.class);
  @Value("${ffmpeg.dir:''}")
  private String videoFolder;
  private Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

  /**
   * 上傳視頻進行切片處理，返回訪問路徑
   *
   * @param video
   * @param transcodeConfig
   * @return
   * @throws IOException
   */
  @PostMapping("upload")
  public Object upload(@RequestPart(name = "file", required = true) MultipartFile video,
                       @RequestPart(name = "config", required = true) TranscodeConfig transcodeConfig) throws IOException {

    logger.info("文件信息：title={}, size={}", video.getOriginalFilename(), video.getSize());
    logger.info("轉碼配置：{}", transcodeConfig);

    // 原始文件名稱，也就是視頻的標題
    String title = video.getOriginalFilename();

    // io到臨時文件
    Path tempFile = tempDir.resolve(title);
    logger.info("io到臨時文件：{}", tempFile.toString());

    try {

      video.transferTo(tempFile);

      // 刪除後綴
      title = title.substring(0, title.lastIndexOf("."));

      // 按照日期生成子目錄
      String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());

      // 嘗試創建視頻目錄
      Path targetFolder = Files.createDirectories(Paths.get(videoFolder, today, title));

      logger.info("創建文件夾目錄：{}", targetFolder);
      Files.createDirectories(targetFolder);

      // 執行轉碼操作
      logger.info("開始轉碼");
      try {
        FFmpegUtils.transcodeToM3u8(tempFile.toString(), targetFolder.toString(), transcodeConfig);
      } catch (Exception e) {
        logger.error("轉碼異常：{}", e.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
      }

      // 封裝結果
      Map<String, Object> videoInfo = new HashMap<>();
      videoInfo.put("title", title);
      videoInfo.put("m3u8", String.join("/", "", today, title, "index.m3u8"));
      videoInfo.put("poster", String.join("/", "", today, title, "poster.jpg"));

      Map<String, Object> result = new HashMap<>();
      result.put("success", true);
      result.put("data", videoInfo);
      return result;
    } finally {
      // 始終刪除臨時文件
      Files.delete(tempFile);
    }
  }
}
