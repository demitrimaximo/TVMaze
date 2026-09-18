package com.api.tvmaze.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tvMazeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TV Maze API")
                        .description("REST API for TV Maze shows and comments")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TV Maze API")));
    }
}