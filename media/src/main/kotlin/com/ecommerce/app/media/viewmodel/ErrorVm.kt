package com.ecommerce.app.media.viewmodel

data class ErrorVm(
    val statusCode: String? = null,
    val title: String? = null,
    val detail: String? = null,
    val fieldErrors: List<String>? = mutableListOf()
)
