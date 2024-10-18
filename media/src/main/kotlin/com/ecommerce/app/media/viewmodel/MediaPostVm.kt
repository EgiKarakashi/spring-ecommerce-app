package com.ecommerce.app.media.viewmodel

import com.ecommerce.app.media.utils.ValidFileType
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class MediaPostVm(
    val caption: String? = null,
    @field:NotNull
    @ValidFileType(allowedTypes = ["image/jpeg", "image/png", "image/git"],
        message = "File type not allowed. Allowed types are JPEG, PNG, GIF")
    val multipartFile: MultipartFile? = null,
    val fileNameOverride: String? = null
)
