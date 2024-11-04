package com.ecommerce.app.commonlibrary.kafka.cdc.message

import com.fasterxml.jackson.annotation.JsonProperty

data class Product(
    val id: Long,
    @JsonProperty("is_published")
    val isPublished: Boolean
)
