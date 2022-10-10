package com.binghe.springdocopenapiswagger.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi userPublicApi() {
        return GroupedOpenApi.builder()
                .group("user-public-v1-definition")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public OpenAPI springdocSwaggerOpenApi() {
        return new OpenAPI()
                .info(new Info().title("springdoc-openapi-swagger 학습 테스트 (User CRUD)")
                        .description("springdoc기반의 swagger 학습테스트로 만든 Swagger 화면입니다.")
                        .version("v0.0.1"));
    }
}
