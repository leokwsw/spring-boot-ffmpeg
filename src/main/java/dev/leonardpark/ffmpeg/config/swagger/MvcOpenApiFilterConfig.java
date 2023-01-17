package dev.leonardpark.ffmpeg.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Component
public class MvcOpenApiFilterConfig implements WebMvcOpenApiTransformationFilter {
  @Override
  public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
    OpenAPI openAPI = context.getSpecification();
    Server localServer = new Server();
    localServer.setUrl(openAPI.getServers().get(0).getUrl() + "/");
    openAPI.setServers(Collections.singletonList(localServer));
    return openAPI;
  }

  @Override
  public boolean supports(DocumentationType documentationType) {
    return documentationType.equals(DocumentationType.OAS_30);
  }
}
