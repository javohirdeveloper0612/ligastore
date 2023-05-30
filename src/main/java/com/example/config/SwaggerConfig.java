package com.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author Firdavs Amonov
 * @version 1.0
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "LigaStore.com APi documentation", version = "1.0"))
@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SwaggerConfig {


}

