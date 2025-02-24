package com.ecommerce.app.springecommerceapp.vector.common.document

import org.springframework.ai.document.ContentFormatter
import org.springframework.ai.document.DefaultContentFormatter
import org.springframework.ai.document.Document
import org.springframework.ai.document.id.IdGenerator

open class BaseDocument(
    private val entityId: Long? = null,
    var content: String? = null,
    var metadata: Map<String, Any>? = null,
    private val contentFormatter: ContentFormatter? = null
) {

    companion object {
        val DEFAULT_CONTENT_FORMATTER = DefaultContentFormatter.builder()
            .from(DefaultContentFormatter.defaultConfig())
            .withTextTemplate(DocumentMetadata.DEFAULT_CONTENT_FORMATTER)
            .build()
    }

    fun toDocument(idGenerator: IdGenerator): Document {
        require(this.javaClass.isAnnotationPresent(DocumentMetadata::class.java)) {
            "Document must be annotated with @DocumentMetadata"
        }
        requireNotNull(content) {"Document's content cannot be null"}
        requireNotNull(metadata) {"Document's metadata cannot be null"}
        return Document(content, metadata, idGenerator).apply {
            contentFormatter = this@BaseDocument.contentFormatter ?: DEFAULT_CONTENT_FORMATTER
        }
    }
}
