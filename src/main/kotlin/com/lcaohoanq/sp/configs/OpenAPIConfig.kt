package com.lcaohoanq.sp.configs

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = io.swagger.v3.oas.annotations.info.Info(
        title = "Shoppe Service API",
        description = "Shoppe API documentation",
        version = "1.0",
    ),
    security = [io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "keycloak")],
    servers = [
        Server(url = "/", description = "Default Server URL"),
        Server(url = "http://localhost:8080", description = "Local development"),
        Server(url = "\${API_SERVER_URL:https://api.example.com}", description = "Production Server")
    ]
)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
    name = "keycloak",
    scheme = "bearer",
    type = SecuritySchemeType.OPENIDCONNECT,
    `in` = SecuritySchemeIn.HEADER,
    openIdConnectUrl = "\${OPENID_CONFIG_URL_PROD}"
)
class OpenAPIConfig {
    companion object {
        const val BEARER_KEY_SECURITY_SCHEME = "bearer-key"
    }
}