package com.ecommerce.app.product.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FIELD, VALUE_PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = [PriceValidator::class])
@MustBeDocumented
annotation class ValidateProductPrice(
    val message: String = "Price must be greater than 0",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
