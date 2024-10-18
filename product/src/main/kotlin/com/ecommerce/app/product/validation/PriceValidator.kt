package com.ecommerce.app.product.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PriceValidator: ConstraintValidator<ValidateProductPrice, Double> {

    override fun initialize(constraintAnnotation: ValidateProductPrice?) {

    }

    override fun isValid(
        productPrice: Double?,
        context: ConstraintValidatorContext?
    ): Boolean {
        return productPrice != null && productPrice >= 0
    }
}
