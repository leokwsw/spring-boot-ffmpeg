package dev.leonardpark.ffmpeg.utils;

import com.google.gson.Gson;
import dev.leonardpark.ffmpeg.model.MediaInfo;
import dev.leonardpark.ffmpeg.model.TranscodeConfig;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.KeyGenerator;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FFmpegUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(FFmpegUtils.class);

  // 跨平臺換行符
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * 生成隨機16個字節的AESKEY
   *
   * @return byte array
   */
  private static byte[] genAesKey() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
      keyGenerator.init(128);
      return keyGenerator.generateKey().getEncoded();
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
  }

  /**
   * 在指定的目錄下生成key_info, key文件，返回key_info文件
   *
   * @param folder folder path
   * @throws IOException IO Exception
   */
  private static Path genKeyInfo(String folder) throws IOException {
    // AES 密鑰
    byte[] aesKey = genAesKey();
    // AES 向量
    String iv = Hex.encodeHexString(genAesKey());

    // key 文件寫入
    Path keyFile = Paths.get(folder, "key");
    Files.write(keyFile, aesKey, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    // key_info 文件寫入
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("key").append(LINE_SEPARATOR);                 // m3u8加載key文件網絡路徑
    stringBuilder.append(keyFile.toString()).append(LINE_SEPARATOR);    // FFmeg加載key_info文件路徑
    stringBuilder.append(iv);                                           // ASE 向量

    Path keyInfo = Paths.get(folder, "key_info");

    Files.write(keyInfo, stringBuilder.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    return keyInfo;
  }

  /**
   * 指定的目錄下生成 master index.m3u8 文件
   *
   * @param file      master m3u8文件地址
   * @param indexPath 訪問子index.m3u8的路徑
   * @param bandWidth 流碼率
   * @throws IOException IO Exception
   */
  private static void genIndex(String file, String indexPath, String bandWidth) throws IOException {
    String stringBuilder = "#EXTM3U" + LINE_SEPARATOR +
      "#EXT-X-STREAM-INF:BANDWIDTH=" + bandWidth + LINE_SEPARATOR +  // 碼率
      indexPath;
    Files.writeString(Paths.get(file), stringBuilder, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  /**
   * 轉碼視頻爲m3u8
   *
   * @param source     源視頻
   * @param destFolder 目標文件夾
   * @param config     配置信息
   * @throws IOException          IO Exception
   * @throws InterruptedException Interrupted Exception
   */
  public static void transcodeToM3u8(String source, String destFolder, TranscodeConfig config) throws IOException, InterruptedException {

    // 判斷源視頻是否存在
    if (!Files.exists(Paths.get(source))) {
      throw new IllegalArgumentException("文件不存在：" + source);
    }

    // 創建工作目錄
    Path workDir = Paths.get(destFolder, "ts");
    Files.createDirectories(workDir);

    // 在工作目錄生成KeyInfo文件
    Path keyInfo = genKeyInfo(workDir.toString());

    // 構建命令
    List<String> commands = new ArrayList<>();
    commands.add("ffmpeg");
    commands.add("-i");
    commands.add(source);                  // 源文件
    commands.add("-c:v");
    commands.add("libx264");               // 視頻編碼爲H264
    commands.add("-c:a");
    commands.add("copy");                  // 音頻直接copy
    commands.add("-hls_key_info_file");
    commands.add(keyInfo.toString());      // 指定密鑰文件路徑
    commands.add("-hls_time");
    commands.add(config.getTsSeconds());   // ts切片大小
    commands.add("-hls_playlist_type");
    commands.add("vod");                   // 點播模式
    commands.add("-hls_segment_filename");
    commands.add("%06d.ts");               // ts切片文件名稱

    if (StringUtils.hasText(config.getCutStart())) {
      commands.add("-ss");
      commands.add(config.getCutStart());    // 開始時間
    }
    if (StringUtils.hasText(config.getCutEnd())) {
      commands.add("-to");
      commands.add(config.getCutEnd());      // 結束時間
    }
    commands.add("index.m3u8");                                                     // 生成m3u8文件

    // 構建進程
    Process process = new ProcessBuilder()
      .command(commands)
      .directory(workDir.toFile())
      .start();

    // 讀取進程標準輸出
    new Thread(() -> {
      try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
          LOGGER.info(line);
        }
      } catch (IOException e) {
        // ignore
      }
    }).start();

    // 讀取進程異常輸出
    new Thread(() -> {
      try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
          LOGGER.info(line);
        }
      } catch (IOException e) {
        // ignore
      }
    }).start();

    // 阻塞直到任務結束
    if (process.waitFor() != 0) {
      throw new RuntimeException("視頻切片異常");
    }

    // 切出封面
    if (!screenShots(source, String.join(File.separator, destFolder, "poster.jpg"), config.getPoster())) {
      throw new RuntimeException("封面截取異常");
    }

    // 獲取視頻信息
    MediaInfo mediaInfo = getMediaInfo(source);
    if (mediaInfo == null) {
      throw new RuntimeException("獲取媒體信息異常");
    }

    // 生成index.m3u8文件
    genIndex(String.join(File.separator, destFolder, "index.m3u8"), "ts/index.m3u8", mediaInfo.getFormat().getBitRate());

    // 刪除keyInfo文件
    Files.delete(keyInfo);
  }

  /**
   * 獲取視頻文件的媒體信息
   *
   * @param source string
   * @return MediaInfo
   * @throws IOException          IOException
   * @throws InterruptedException Interrupted Exception
   */
  public static MediaInfo getMediaInfo(String source) throws IOException, InterruptedException {
    List<String> commands = new ArrayList<>();
    commands.add("ffprobe");
    commands.add("-i");
    commands.add(source);
    commands.add("-show_format");
    commands.add("-show_streams");
    commands.add("-print_format");
    commands.add("json");

    Process process = new ProcessBuilder(commands)
      .start();

    MediaInfo mediaInfo = null;

    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      mediaInfo = new Gson().fromJson(bufferedReader, MediaInfo.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (process.waitFor() != 0) {
      return null;
    }

    return mediaInfo;
  }

  /**
   * 截取視頻的指定時間幀，生成圖片文件
   *
   * @param source 源文件
   * @param file   圖片文件
   * @param time   截圖時間 HH:mm:ss.[SSS]
   * @throws IOException          IO Exception
   * @throws InterruptedException Interrupted Exception
   */
  public static boolean screenShots(String source, String file, String time) throws IOException, InterruptedException {

    List<String> commands = new ArrayList<>();
    commands.add("ffmpeg");
    commands.add("-i");
    commands.add(source);
    commands.add("-ss");
    commands.add(time);
    commands.add("-y");
    commands.add("-q:v");
    commands.add("1");
    commands.add("-frames:v");
    commands.add("1");
    commands.add("-f");
    commands.add("image2");
    commands.add(file);

    Process process = new ProcessBuilder(commands)
      .start();

    // 讀取進程標準輸出
    new Thread(() -> {
      try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
          LOGGER.info(line);
        }
      } catch (IOException e) {
        // ignore
      }
    }).start();

    // 讀取進程異常輸出
    new Thread(() -> {
      try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
          LOGGER.error(line);
        }
      } catch (IOException e) {
        // ignore
      }
    }).start();

    return process.waitFor() == 0;
  }
}
