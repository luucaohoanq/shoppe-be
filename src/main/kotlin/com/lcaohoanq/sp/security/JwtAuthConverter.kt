package com.lcaohoanq.sp.security

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
class JwtAuthConverter(
    private val properties: JwtAuthConverterProperties
) : Converter<Jwt, AbstractAuthenticationToken> {

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities = mutableSetOf<GrantedAuthority>()
        
        // Add authorities from default JWT converter
        authorities += jwtGrantedAuthoritiesConverter.convert(jwt).orEmpty()
        
        // Add authorities from Keycloak realm_access
        authorities += extractRealmRoles(jwt)
        
        // Add authorities from Keycloak resource_access
        authorities += extractResourceRoles(jwt)

        // Determine the principal - use preferred_username if available, otherwise sub
        val principalClaim = properties.principalAttribute ?: JwtClaimNames.SUB
        val principal = jwt.getClaim<String>(principalClaim) 
            ?: jwt.getClaim<String>("preferred_username")
            ?: jwt.getClaim<String>("email")
            ?: jwt.subject

        return JwtAuthenticationToken(jwt, authorities, principal)
    }

    private fun extractRealmRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val realmAccess = jwt.getClaim<Map<String, Any>?>("realm_access") ?: return emptySet()
        val roles = realmAccess["roles"] as? Collection<*> ?: return emptySet()
        
        return roles.filterIsInstance<String>().map { SimpleGrantedAuthority("ROLE_$it") }.toSet()
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess = jwt.getClaim<Map<String, Any>?>("resource_access") ?: return emptySet()

        val resource = resourceAccess[properties.resourceId] as? Map<*, *> ?: return emptySet()
        val roles = resource["roles"] as? Collection<*> ?: return emptySet()

        return roles.filterIsInstance<String>().map { SimpleGrantedAuthority("ROLE_$it") }.toSet()
    }

    companion object {
        private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
    }
}

@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
class JwtAuthConverterProperties {
    @NotBlank
    lateinit var resourceId: String
    var principalAttribute: String? = null
}
