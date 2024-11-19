package com.ecommerce.app.order.viewmodel

data class ErrorVm(val statusCode: String, val title: String, val detail: String?, val fieldErrors: List<String>?)
