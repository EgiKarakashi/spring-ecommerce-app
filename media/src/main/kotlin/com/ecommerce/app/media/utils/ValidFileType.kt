package com.ecommerce.app.media.utils

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [FileTypeValidator::class])
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidFileType(
    val message: String = "Invalid file type",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val allowedTypes: Array<String>
)

