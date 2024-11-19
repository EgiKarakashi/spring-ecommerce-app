package com.ecommerce.app.order.model.request

import com.ecommerce.app.order.model.enumeration.OrderStatus
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.ZonedDateTime

data class OrderRequest(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var createdFrom: ZonedDateTime? = null,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var createdTo: ZonedDateTime? = null,

    @JsonProperty
    var warehouse: String? = null,

    @JsonProperty
    var productName: String? = null,

    @JsonProperty
    var orderStatus: List<OrderStatus>? = null,

    @JsonProperty
    var billingPhoneNumber: String? = null,

    @JsonProperty
    var email: String? = null,

    @JsonProperty
    var billingCountry: String? = null,

    @JsonProperty
    var pageNo: Int = 0,

    @JsonProperty
    var pageSize: Int = 0
)
