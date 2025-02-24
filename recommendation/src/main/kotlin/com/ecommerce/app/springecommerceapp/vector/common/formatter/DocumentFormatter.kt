package com.ecommerce.app.springecommerceapp.vector.common.formatter

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.regex.Pattern

interface DocumentFormatter {
    companion object {
        val HTML_TAG_PATTERN: Pattern = Pattern.compile("<[^>]*>")
    }

    fun format(entityMap: MutableMap<String, Any>, template: String, objectMapper: ObjectMapper): String

    fun removeHtmlTags(input: String?): String {
        if (input.isNullOrEmpty()) {
            return input.orEmpty()
        }
        return HTML_TAG_PATTERN.matcher(input).replaceAll("").trim()
    }
}
