package com.ecommerce.app.product.utils

object ProductConverter {
    fun toSlug(input: String): String {
        val slug = input.trim().lowercase()
            .replace(Regex("[^a-z0-9\\-]"), "-")
            .replace(Regex("-{2,}"), "-")

        return if (slug.startsWith("-")) slug.substring(1) else slug
    }
}
