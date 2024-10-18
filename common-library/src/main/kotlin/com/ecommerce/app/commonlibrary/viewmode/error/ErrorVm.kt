package com.ecommerce.app.commonlibrary.viewmode.error

data class ErrorVm(val statusCode: String, val title: String, val detail: String?, val fieldErrors: List<String>?)
