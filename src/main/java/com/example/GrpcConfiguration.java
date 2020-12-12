package com.example;

import brave.grpc.GrpcTracing;
import io.grpc.ClientInterceptor;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(GrpcConfiguration.class);

  @Value("${scenario}")
  private String scenario;
  @Value("${spring.sleuth.enabled}")
  private boolean isSleuthEnabled;

  @PostConstruct
  public void logState() {
    LOGGER.warn("scenario: {}", scenario);
    LOGGER.warn("sleuth enabled: {}", isSleuthEnabled);
  }

  /* Doesn't work despite spring.sleuth.enabled=true */
  @Bean
  @ConditionalOnBean(GrpcTracing.class)
  @ConditionalOnProperty(value = "scenario", havingValue = "conditional-on-bean")
  public ClientInterceptor conditionalOnBeanScenario(GrpcTracing grpcTracing) {
    return grpcTracing.newClientInterceptor();
  }

  /* Works, workaround for above */
  @Bean
  @ConditionalOnProperty(value = "scenario", havingValue = "optional-bean")
  public ClientInterceptor optionalBeanScenario(Optional<GrpcTracing> grpcTracing) {
    return grpcTracing.map(GrpcTracing::newClientInterceptor).orElse(null);
  }

  /* Works, but throws when I disable spring.sleuth.enabled since GrpcTracing is not provided */
  @Bean
  @ConditionalOnProperty(value = "scenario", havingValue = "unconditional-bean")
  public ClientInterceptor unconditionalBeanScenario(GrpcTracing grpcTracing) {
    return grpcTracing.newClientInterceptor();
  }

  @Bean
  public String injectClientInterceptors(List<ClientInterceptor> clientInterceptors) {
    // should have 1 interceptor when spring.sleuth.enabled=true and 0 when spring.sleuth.enabled=false
    LOGGER.warn("Found {} client interceptor(s)", clientInterceptors.size());
    return "done";
  }
}
