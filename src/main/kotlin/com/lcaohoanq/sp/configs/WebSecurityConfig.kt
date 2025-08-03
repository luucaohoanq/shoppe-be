package com.lcaohoanq.sp.configs

import com.lcaohoanq.sp.security.JwtAuthConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
class WebSecurityConfig(
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
    private val corsConfig: CorsConfig,
    private val jwtAuthConverter: JwtAuthConverter
) {
    @Value("\${api.prefix}")
    private lateinit var apiPrefix: String

    // Use a companion object for constants
    companion object {
        private val PUBLIC_ENDPOINTS = arrayOf(
            "/actuator/**",
            "/api/v1/h2-console/**",
            "/graphiql",
            "/graphql",
            "/error",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/auth/**",
            "/api/v1/categories/all",
            "/api/v1/categories/parents",
            "/api/v1/categories/*/children"
        )
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors ->
                cors.configurationSource(corsConfig.corsConfigurationSource())
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session
            }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(*PUBLIC_ENDPOINTS).permitAll()
                auth.anyRequest().permitAll()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthConverter)
                }
            }
            .csrf { it.disable() }
            .exceptionHandling { ex ->
                ex.authenticationEntryPoint(authenticationEntryPoint)
                ex.accessDeniedHandler(accessDeniedHandler)
            }

        return http.build()
    }
}
