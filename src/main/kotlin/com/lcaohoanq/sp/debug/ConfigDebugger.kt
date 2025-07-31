package com.lcaohoanq.sp.debug

import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class ConfigDebugger(
    private val environment: Environment // Inject the Environment
) {

    private val log = KotlinLogging.logger {}

    @Value("\${spring.datasource.url}")
    private lateinit var datasourceUrl: String

    @Value("\${spring.datasource.driver-class-name}")
    private lateinit var driverClassName: String

    @PostConstruct
    fun debugConfig() {
        println("Active profiles: ${environment.activeProfiles.joinToString()}")
        println("Datasource URL: $datasourceUrl")
        println("Driver class: $driverClassName")
    }
}