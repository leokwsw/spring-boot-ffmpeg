package dev.leonardpark.ffmpeg.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseUtils {
  public static ResponseEntity<Resource> fileResponse(File file) throws IOException {

    String fileName = file.getName();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
    InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

    String mime = FileUtil.getFileMime(file.toPath());

    return fileResponse(fileName, inputStreamResource, mime);
  }

  public static ResponseEntity<Resource> fileResponse(
    String fileName,
    InputStreamResource file,
    String mime
  ) {
    fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

    return ResponseEntity.status(HttpStatus.OK)
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
      .contentType(MediaType.parseMediaType(mime))
      .body(file);
  }
}
