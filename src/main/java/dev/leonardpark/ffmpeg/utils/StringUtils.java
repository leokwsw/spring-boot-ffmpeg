package dev.leonardpark.ffmpeg.utils;

public class StringUtils {
  public static String escapeRegExp(String str){
    return str.replace("/[.*+?^${}()|[\\]\\\\]/g", "\\$&");
  }
}
