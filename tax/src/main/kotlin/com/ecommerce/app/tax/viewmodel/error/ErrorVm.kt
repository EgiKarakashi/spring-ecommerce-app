package com.ecommerce.app.tax.viewmodel.error

data class ErrorVm(val statusCode: String, val title: String, val detail: String?, val fieldErrors: List<String>?)
