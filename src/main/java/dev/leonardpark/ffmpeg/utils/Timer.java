package dev.leonardpark.ffmpeg.utils;

import java.sql.Timestamp;

public class Timer {
  private final Timestamp timestamp;

  public Timer() {
    long datetime = System.currentTimeMillis();
    timestamp = new Timestamp(datetime);
  }

  public double timePast() {
    long currDatetime = System.currentTimeMillis();
    return (new Timestamp(currDatetime).getTime() - timestamp.getTime()) / 1000d;
  }
}
