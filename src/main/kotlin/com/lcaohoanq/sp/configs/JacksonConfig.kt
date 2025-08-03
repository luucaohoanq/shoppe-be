package com.lcaohoanq.sp.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class JacksonConfig {

    private val dateTimePattern = "yyyy-MM-dd HH:mm:ss"
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern)

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val module = SimpleModule()
        module.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(dateTimeFormatter))
        module.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(dateTimeFormatter))

        return Jackson2ObjectMapperBuilder.json()
            .modules(module, JavaTimeModule())
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()
    }
}