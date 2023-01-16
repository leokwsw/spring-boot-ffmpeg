package dev.leonardpark.ffmpeg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
  @Override
  protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    configurer.setDefaultTimeout(300000);
    configurer.registerCallableInterceptors(timeoutInterceptor());
  }

  @Bean
  public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
    return new TimeoutCallableProcessingInterceptor();
  }
}
