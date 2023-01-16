package dev.leonardpark.ffmpeg.config.swagger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableSwagger2
public class Config {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.OAS_30)
      .groupName("API")
      .apiInfo(new ApiInfoBuilder()
        .title("FFMpeg API")
        .version("0.0.0")
        .build())
      .pathProvider(new PathProvider() {
        @Override
        public String getOperationPath(String operationPath) {
          return operationPath;
        }

        @Override
        public String getResourceListingPath(String groupName, String apiDeclaration) {
          return "/";
        }
      })
      .select()
      .apis(RequestHandlerSelectors.basePackage("dev.leonardpark.ffmpeg.controller"))
      .paths(PathSelectors.any())
      .build()
      .useDefaultResponseMessages(false);
  }

//  @Bean
//  public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
//    WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier,
//    ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
//    CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
//    List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
//    Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//    allEndpoints.addAll(webEndpoints);
//    allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//    allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//    String basePath = webEndpointProperties.getBasePath();
//    EndpointMapping endpointMapping = new EndpointMapping(basePath);
//    boolean shouldRegisterLinksMapping = webEndpointProperties.getDiscovery().isEnabled() &&
//      (org.springframework.util.StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
//    return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping);
//  }

  @Bean
  public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
    return new BeanPostProcessor() {

      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
          customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
        }
        return bean;
      }

      private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
        List<T> copy = mappings.stream()
          .filter(mapping -> mapping.getPatternParser() == null)
          .collect(Collectors.toList());
        mappings.clear();
        mappings.addAll(copy);
      }

      @SuppressWarnings("unchecked")
      private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
        try {
          Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
          field.setAccessible(true);
          return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          throw new IllegalStateException(e);
        }
      }
    };
  }
}
