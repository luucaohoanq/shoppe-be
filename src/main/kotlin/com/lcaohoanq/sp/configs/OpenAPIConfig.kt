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
        Server(url = "\${http://localhost:4006/}", description = "Local server"),
        Server(url = "\${https://api.shoppe.com}", description = "Production server")
    ]
)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
    name = "keycloak",
    type = SecuritySchemeType.OPENIDCONNECT,
    scheme = "bearer",
    `in` = SecuritySchemeIn.HEADER,
    openIdConnectUrl = "\${KEYCLOAK_OPENID_CONFIG_URL:http://localhost:9098/realms/shoppe/.well-known/openid-configuration}"
)
class OpenAPIConfig {
    companion object {
        const val BEARER_KEY_SECURITY_SCHEME = "bearer-key"
    }
}