package com.bluesoft.pawmarket.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI pawMarketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PawMarket API")
                        .description("REST API for a pet products marketplace — catalog, users and orders.")
                        .version("v1.0.0"));
    }
}
