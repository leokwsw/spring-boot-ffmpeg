package dev.leonardpark.ffmpeg.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog extends Base {
  private String className;
  private String username;
  private String methodName;
  private String requestMethod;
  private String requestPath;
  private Object requestHeader;
  private String requestRemoteHost;
  private Object requestParams;
  private Object requestBody;
  private String requestUserAgent;
  private Integer responseStatusCode;

  public AuditLog(String className, HttpServletRequest request, String methodName, ResponseEntity<Object> responseEntity) {
    this.className = className;
    this.username = request.getRemoteUser();
    this.methodName = methodName;
    this.requestMethod = request.getMethod();
    this.requestPath = request.getRequestURL().toString();
    this.requestHeader = convertKeysToHashMap(request, request.getHeaderNames());
    this.requestRemoteHost = getRemoteIp(request);
    this.requestParams = convertKeysToHashMap(request, request.getParameterNames());
    this.requestBody = convertKeysToHashMap(request, request.getParameterNames());
    this.requestUserAgent = request.getHeader("User-Agent");
    this.responseStatusCode = responseEntity.getStatusCode().value();
  }

  private HashMap<String, String> convertKeysToHashMap(HttpServletRequest request, Enumeration<String> keys) {
    HashMap<String, String> headerObj = new HashMap<>();

    for (String key : Collections.list(keys)) {
      headerObj.put(key, request.getHeader(key));
    }
    return headerObj;
  }

  private String getRemoteIp(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
