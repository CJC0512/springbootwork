package com.ohgiraffers.restapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/* 설명. Swagger는  OpenAPI Specification(OAS)이다. */
/* 설명. http://localhost:8080/swagger-ui/index.html */
@OpenAPIDefinition(
        info = @Info(title = "Lecture API 명세서",
                description = "수업용 API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {

}