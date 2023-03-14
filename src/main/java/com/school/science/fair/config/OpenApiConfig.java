package com.school.science.fair.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().info(
        new Info()
            .title("Science Fair")
            .description("Science Fair System")
            .version("0.0.1"));
  }


}
