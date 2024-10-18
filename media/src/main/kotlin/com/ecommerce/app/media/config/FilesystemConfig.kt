package com.ecommerce.app.media.config

import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class FilesystemConfig : WebMvcConfigurer {

    @Value("\${file.directory}")
    lateinit var directory: String
}


