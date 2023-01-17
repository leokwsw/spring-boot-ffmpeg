package dev.leonardpark.ffmpeg.utils;

import org.apache.tika.Tika;

import java.io.IOException;
import java.nio.file.Path;

public class FileUtil {
  public static String getFileMime(Path path) throws IOException {
    Tika tika = new Tika();
    return tika.detect(path);
  }

  public static String getPathExt(String filePath) {
    String[] splinted = filePath.split("\\.");
    return splinted[splinted.length - 1];
  }
}
