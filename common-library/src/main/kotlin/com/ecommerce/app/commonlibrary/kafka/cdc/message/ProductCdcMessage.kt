package com.ecommerce.app.commonlibrary.kafka.cdc.message

import jakarta.validation.constraints.NotNull

data class ProductCdcMessage(
    @NotNull
    val after: Product,
    val before: Product,
    @NotNull
    val op: Operation
)
