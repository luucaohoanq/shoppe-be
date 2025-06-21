package com.lcaohoanq.sp.configs

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakClientConfig(
 private val keycloakPropsConfig:  KeycloakPropsConfig
) {

    @Bean
    fun keycloak(): Keycloak{
        return KeycloakBuilder.builder()
            .serverUrl(keycloakPropsConfig.serverUrl)
            .realm(keycloakPropsConfig.realm)
            .grantType(keycloakPropsConfig.grantType)
            .clientId(keycloakPropsConfig.clientId)
            .username(keycloakPropsConfig.username)
            .password(keycloakPropsConfig.password)
            .clientSecret(keycloakPropsConfig.clientSecret)
            .build()
    }

}
@Configuration
@ConfigurationProperties(prefix = "keycloak")
data class KeycloakPropsConfig(

    var url: String = "",
    var serverUrl: String = "",
    var realm: String = "",
    var clientId: String = "",
    var grantType: String = "password",
    var password: String = "",
    var username: String = "",
    var clientSecret: String = ""

) {}