package com.ecommerce.app.media.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Configuration
@EnableWebMvc
class FilesystemConfig : WebMvcConfigurer {

    @Value("\${file.directory}")
    lateinit var directory: String
}


