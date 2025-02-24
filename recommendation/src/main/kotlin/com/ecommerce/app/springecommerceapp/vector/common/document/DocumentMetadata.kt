package com.ecommerce.app.springecommerceapp.vector.common.document

import com.ecommerce.app.springecommerceapp.vector.common.formatter.DocumentFormatter
import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DocumentMetadata(
    val docIdPrefix: String,
    val contentFormat: String,
    val embeddingContentFormatter:  String = DEFAULT_CONTENT_FORMATTER,
    val documentFormatter: KClass<out DocumentFormatter>
) {

    companion object {
        const val DEFAULT_CONTENT_FORMATTER = "{content}"
    }
}
