package com.ecommerce.app.customer.viewmodel.customer

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CustomerPostVm(
    @field:NotBlank val username: String,
    @field:NotBlank @field:Email val email: String,
    @field:NotBlank val firstName: String,
    @field:NotBlank val lastName: String,
    @field:NotBlank val password: String,
    @field:NotBlank val role: String
)
