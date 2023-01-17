package dev.leonardpark.ffmpeg.controller;

import dev.leonardpark.ffmpeg.model.CommonResponseModel;
import dev.leonardpark.ffmpeg.model.GetFileListQueryModel;
import dev.leonardpark.ffmpeg.service.FileService;
import dev.leonardpark.ffmpeg.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Api(tags = "File")
@RestController
@RequestMapping("/api/file/")
@RequiredArgsConstructor
public class FileController {

  @Autowired
  private FileService fileService;

  @ApiOperation(value = "file list")
  @GetMapping("list")
  // Request : size, limit, search by file name, filter by extension
  // Response : file list with file id, file name, extension, file size, preview url
  public ResponseEntity<CommonResponseModel> getFileList(
    GetFileListQueryModel queryModel
  ) {
    return CommonResponseModel.successResponseWithMessage(true, "file list");
  }

  @ApiOperation(value = "upload file")
  @PostMapping("upload")
  // Request : file
  // Response : file id, file name, extension, file size, preview url
  public ResponseEntity<CommonResponseModel> postFile(
    @RequestPart MultipartFile file
  ) {
    return CommonResponseModel.successResponseWithMessage(true, "upload file");
  }

  @ApiOperation(value = "delete files")
  @DeleteMapping("delete")
  // Request : file id array
  // Response : object of success delete , waiting delete, delete failed
  public ResponseEntity<CommonResponseModel> deleteFiles(
    @RequestBody List<String> fileIds
  ) {
    return CommonResponseModel.successResponseWithMessage(true, "delete files");
  }

  @ApiOperation(value = "get file")
  @GetMapping("{fileId}")
  public ResponseEntity<Resource> getFile(
    @PathVariable("fileId") String fileId
  ) throws IOException {
    String dummyFile = "/Users/leowu/Desktop/sampleVideo/Dr.STONE-新石紀-第二季/第06話-越獄.mp4";
    File file = new File(dummyFile);

    return ResponseUtils.fileResponse(file);
  }
}
