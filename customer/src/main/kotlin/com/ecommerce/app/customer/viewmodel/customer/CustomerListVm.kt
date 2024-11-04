package com.ecommerce.app.customer.viewmodel.customer

data class CustomerListVm(
    val totalUser: Int,
    val customers: List<CustomerAdminVm>,
    val totalPage: Int
)
