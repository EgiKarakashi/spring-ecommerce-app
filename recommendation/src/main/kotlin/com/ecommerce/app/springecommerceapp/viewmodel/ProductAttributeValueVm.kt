package com.ecommerce.app.springecommerceapp.viewmodel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductAttributeValueVm(
    val id: Long,
    val nameProductAttribute: String,
    val value: String
)
