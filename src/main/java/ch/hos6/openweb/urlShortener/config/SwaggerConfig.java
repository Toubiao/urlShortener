package ch.hos6.openweb.urlShortener.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI apiInfo() {
        final var securitySchemeName = "bearer";
        final var basicSecuritySchemeName = "basicAuth";
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT"))
                                .addSecuritySchemes(
                                        basicSecuritySchemeName,
                                        new SecurityScheme()
                                                .name(basicSecuritySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("basic")))
                .info(
                        new Info()
                                .title("Url shortener Rest Api")
                                .description("REST API for the URL shortener service.")
                                .version("1.0"));
    }
}
