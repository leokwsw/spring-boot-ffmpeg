package dev.leonardpark.ffmpeg.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class MD5Hash {
  public static String getMD5Hash(File file) throws IOException {
    if (!file.exists()) throw new IOException("The file is not exists");
    FileInputStream fileInputStream = null;
    DigestInputStream digestInputStream = null;
    byte[] buff = new byte[1024];
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      fileInputStream = new FileInputStream(file);
      digestInputStream = new DigestInputStream(fileInputStream, md);
      while (digestInputStream.read(buff) != -1) ;
      byte[] md5Digests = md.digest();
      return byteArray2Hex(md5Digests);
    } catch (IOException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    } finally {
      buff = null;
      if (fileInputStream != null) fileInputStream.close();
      if (digestInputStream != null) digestInputStream.close();
    }
    return null;
  }

  private static String byteArray2Hex(byte[] hash) {
    Formatter formatter = new Formatter();
    for (byte b : hash) {
      formatter.format("%02x", b);
    }
    return formatter.toString();
  }
}
