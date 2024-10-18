package com.ecommerce.app.product.viewmodel.error

data class ErrorVm(
    val statusCode: String,
    val title: String,
    val detail: String,
    val fieldErrors: List<String> = emptyList(),
)
