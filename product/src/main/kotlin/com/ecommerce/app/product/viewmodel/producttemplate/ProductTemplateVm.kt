package com.ecommerce.app.product.viewmodel.producttemplate

data class ProductTemplateVm(
    val id: Long,
    val name: String,
    val productAttributeTemplates: List<ProductAttributeTemplateGetVm>
)
