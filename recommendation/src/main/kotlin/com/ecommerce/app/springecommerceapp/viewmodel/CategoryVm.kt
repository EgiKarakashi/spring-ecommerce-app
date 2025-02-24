package com.ecommerce.app.springecommerceapp.viewmodel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryVm(
    val id: Long,
    val name: String,
    val description: String,
    val slug: String,
    val metaKeyword: String,
    val metaDescription: String,
    val displayOrder: Short,
    val isPublished: Boolean
)
