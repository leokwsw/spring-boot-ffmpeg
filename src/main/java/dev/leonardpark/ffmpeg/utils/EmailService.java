package dev.leonardpark.ffmpeg.utils;

import dev.leonardpark.ffmpeg.exception.EmailServiceRunTimeException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

@Service
@Component
public class EmailService {
  private final Logger log = LoggerFactory.getLogger(EmailService.class);
  @Value("${email.host}")
  private String senderHost;
  @Value("${email.port:587}")
  private int senderPort;
  @Value("${email.sender.email}")
  private String senderEmail;
  @Value("${email.sender.password}")
  private String senderPassword;
  @Value("${email.sender.from:''}")
  private String from;
  @Value("${email.smtp.auth:true}")
  private boolean smtpAuth;
  @Value("${email.debug:false}")
  private String debug;
  @Value("${email.transport.protocols:smtp}")
  private String transportProtocols;
  @Value("${email.smtp.starttls.enable:true}")
  private String starttlsEnable;

  public EmailService() {

  }

  private void checkSetup() {
    if (this.senderHost == null || this.senderHost.isEmpty())
      throw new EmailServiceRunTimeException("Email Sender Host is not ready to set");
    if (this.senderEmail == null || this.senderEmail.isEmpty())
      throw new EmailServiceRunTimeException("Email Sender Email address is not ready to set");
    if (this.senderPassword == null || this.senderPassword.isEmpty())
      throw new EmailServiceRunTimeException("Email Sender Password is not ready to set");
  }

  private JavaMailSenderImpl mailSender() {
    checkSetup();

    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    if (Objects.equals(debug, "true")) {
      log.info("sender Host:port : {}:{}", this.senderHost, this.senderPort);
      log.info("sender email : {} ; password : {}", this.senderEmail, this.senderPassword);
    }
    sender.setHost(senderHost);
    sender.setPort(senderPort);
    sender.setUsername(senderEmail);
    sender.setPassword(senderPassword);

    Properties properties = sender.getJavaMailProperties();
    properties.put("mail.transport.protocol", transportProtocols);
    properties.put("mail.smtp.auth", smtpAuth);
    properties.put("mail.smtp.starttls.enable", starttlsEnable);
    properties.put("mail.debug", debug);

    sender.setJavaMailProperties(properties);

    return sender;
  }

  public void serverStart() {
    String to = "leo.wu.kw@outlook.com";
    String subject = "FFMpeg Server started";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String body = String.format("Server is started at %s", formatter.format(now));
    sendBodyMail(to, subject, body, false);
  }

  public void serverStop() {
    String to = "leo.wu.kw@outlook.com";
    String subject = "FFMpeg Server Stopped";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String body = String.format("Server is Stopped at %s", formatter.format(now));
    sendBodyMail(to, subject, body, false);
  }

  private void sendBodyMail(String to, String subject, String body, boolean isHtml) {
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        try {
          JavaMailSender mailSender = mailSender();

          MimeMessage mimeMessage = mailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

          helper.setText(body, isHtml);
          helper.setTo(to);
          helper.setSubject(String.format("[ffmpeg server] %s", subject));
          helper.setFrom(from, "Leonardpark Server");

          mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }
    }, 3000);
  }
}
