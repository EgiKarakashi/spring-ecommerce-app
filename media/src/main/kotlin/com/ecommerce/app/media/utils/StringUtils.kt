package com.ecommerce.app.media.utils

import lombok.experimental.UtilityClass

@UtilityClass
object StringUtils {
    fun hasText(input: String?): Boolean {
        return !input.isNullOrBlank()
    }
}

