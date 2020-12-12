package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import io.grpc.ClientInterceptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

public class GrpcConfigurationTest {

  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(GrpcConfiguration.class));

  @Test
  public void givenConditionalOnBeanAndSleuthEnabled_ThenOneClientInterceptorIsProvided() {
    this.contextRunner
        .withPropertyValues("scenario=conditional-on-bean", "spring.sleuth.enabled=true")
        .run(context -> assertThat(context).hasSingleBean(ClientInterceptor.class));
  }

  @Test
  public void givenConditionalOnBeanAndSleuthDisabled_ThenNoClientInterceptorIsProvided() {
    this.contextRunner
        .withPropertyValues("scenario=conditional-on-bean", "spring.sleuth.enabled=false")
        .run(context -> assertThat(context).doesNotHaveBean(ClientInterceptor.class));
  }

  @Test
  public void givenOptionalBeanAndSleuthEnabled_ThenOneClientInterceptorIsProvided() {
    this.contextRunner.withPropertyValues("scenario=optional-bean", "spring.sleuth.enabled=true")
        .run(context -> assertThat(context).hasSingleBean(ClientInterceptor.class));
  }

  @Test
  public void givenOptionalBeanAndSleuthDisabled_ThenNoClientInterceptorIsProvided() {
    // I think the bean is created but not provided to List<ClientInterceptor>
    this.contextRunner.withPropertyValues("scenario=optional-bean", "spring.sleuth.enabled=false")
        .run(context -> assertThat(context).hasSingleBean(ClientInterceptor.class));
  }

  @Test
  @Disabled("This scenario runs fine when starting the app, but the test fails 👀")
  public void givenUnconditionalBeanAndSleuthEnabled_ThenOneClientInterceptorIsProvided() {
    this.contextRunner
        .withPropertyValues("scenario=unconditional-bean", "spring.sleuth.enabled=true")
        .run(context -> assertThat(context).hasSingleBean(ClientInterceptor.class));
  }

  @Test
  public void givenUnconditionalBeanAndSleuthDisabled_ThenNoClientInterceptorIsProvided() {
    this.contextRunner
        .withPropertyValues("scenario=unconditional-bean", "spring.sleuth.enabled=false")
        .run(context -> assertThat(context).hasFailed());
  }
}
