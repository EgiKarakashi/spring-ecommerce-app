package com.ecommerce.app.tax.viewmodel.taxclass

data class TaxClassListGetVm(
    val taxClassContent: List<TaxClassVm>,
    val pageNo: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int,
    val isLast: Boolean
)
