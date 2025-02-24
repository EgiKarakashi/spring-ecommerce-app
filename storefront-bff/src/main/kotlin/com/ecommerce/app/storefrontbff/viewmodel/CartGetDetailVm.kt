package com.ecommerce.app.storefrontbff.viewmodel

data class CartGetDetailVm(val id: Long, val customerId: String, val cartDetails: List<CartDetailVm>)
