package dev.leonardpark.ffmpeg;

import dev.leonardpark.ffmpeg.utils.EmailService;
import dev.leonardpark.ffmpeg.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Transactional
@IntegrationComponentScan
@EnableIntegration
public class FfmpegSpringbootApplication extends SpringBootServletInitializer {
  private static final Logger log = LoggerFactory.getLogger(FfmpegSpringbootApplication.class);
  static Timer timer = new Timer();

  public static void main(String[] args) {
    SpringApplication.run(FfmpegSpringbootApplication.class, args);
    log.info("RunTime : Server Started ; used : {}", timer.timePast());
  }

  @Bean
  CommandLineRunner run(
    EmailService emailService
  ) {
    return args -> {
      emailService.serverStart();
      log.info("RunTime : CommandLineRunner run Finished");
    };
  }

  @Bean
  CommandLineRunner onExit(
    EmailService emailService
  ) {
    return args -> {
      emailService.serverStop();
      log.info("RunTime : CommandLineRunner onExit Finished");
    };
  }

}
