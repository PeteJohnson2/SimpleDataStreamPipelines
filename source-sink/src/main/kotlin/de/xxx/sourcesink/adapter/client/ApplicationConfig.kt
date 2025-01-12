package de.xxx.sourcesink.adapter.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ApplicationConfig {
    @Bean
    fun createObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper();
        objectMapper.registerModule(JavaTimeModule())
        return objectMapper
    }
}