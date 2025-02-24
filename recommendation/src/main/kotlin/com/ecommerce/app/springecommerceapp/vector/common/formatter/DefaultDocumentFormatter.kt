package com.ecommerce.app.springecommerceapp.vector.common.formatter

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.text.StringSubstitutor

class DefaultDocumentFormatter: DocumentFormatter {
    override fun format(entityMap: MutableMap<String, Any>, template: String, objectMapper: ObjectMapper): String {
        val sub = StringSubstitutor(entityMap, "{", "}")
        return removeHtmlTags(sub.replace(template))
    }
}
