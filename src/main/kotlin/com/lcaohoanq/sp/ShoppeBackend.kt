package com.lcaohoanq.sp

import io.github.hoangclw.kotlinbrowserlauncher.openHomePage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableCaching
@EnableScheduling
class ShoppeBackend

fun main(args: Array<String>) {
    val context = runApplication<ShoppeBackend>(*args)
    val env = context.environment
    val activeProfiles = env.activeProfiles

    if (!activeProfiles.contains("docker")) {
        openHomePage("http://localhost:4006/swagger-ui/index.html")
    }
}
