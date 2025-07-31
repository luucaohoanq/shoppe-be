package com.lcaohoanq.sp

import io.github.lcaohoanq.annotations.BrowserLauncher
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableCaching
@EnableScheduling
@BrowserLauncher(
    value = "http://localhost:4006/swagger-ui/index.html",
    healthCheckEndpoint = "http://localhost:4006/actuator/health"
)
class ShoppeBackend

fun main(args: Array<String>) {
    runApplication<ShoppeBackend>(*args)
}
