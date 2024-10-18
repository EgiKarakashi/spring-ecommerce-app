package com.ecommerce.app.media.model.dto

import org.springframework.http.MediaType
import java.io.InputStream

data class MediaDto(
    val content: InputStream? = null,
    val mediaType: MediaType? = null
)
