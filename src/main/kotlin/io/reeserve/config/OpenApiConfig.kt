package io.reeserve.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"

        // 1) Define the security scheme type (HTTP bearer)
        val securityScheme = SecurityScheme()
            .name(securitySchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")

        // 2) Apply the security globally to all operations
        val securityRequirement = SecurityRequirement().addList(securitySchemeName)

        return OpenAPI()
            .components(Components().addSecuritySchemes(securitySchemeName, securityScheme))
            .addSecurityItem(securityRequirement)
    }
}
