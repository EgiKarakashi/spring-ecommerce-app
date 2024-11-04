package com.ecommerce.app.search.viewmodel.error

data class ErrorVm(
    val statusCode: String,
    val title: String,
    val detail: String,
    val fieldErrors: List<String> = emptyList(),
)
