package com.ecommerce.app.order.model.csv

import com.ecommerce.app.commonlibrary.csv.BaseCsv
import com.ecommerce.app.commonlibrary.csv.annotation.CsvColumn
import com.ecommerce.app.commonlibrary.csv.annotation.CsvName
import com.ecommerce.app.order.model.enumeration.DeliveryStatus
import com.ecommerce.app.order.model.enumeration.OrderStatus
import com.ecommerce.app.order.model.enumeration.PaymentStatus

import java.math.BigDecimal
import java.time.ZonedDateTime

@CsvName(fileName = "Orders")
open class OrderItemCsv(
    @CsvColumn(columnName = "Order status")
    var orderStatus: OrderStatus? = null,

    @CsvColumn(columnName = "Payment status")
    var paymentStatus: PaymentStatus? = null,

    @CsvColumn(columnName = "Email")
    var email: String? = null,

    @CsvColumn(columnName = "Phone")
    var phone: String? = null,

    @CsvColumn(columnName = "Order total")
    var totalPrice: BigDecimal? = null,

    @CsvColumn(columnName = "Shipping status")
    var deliveryStatus: DeliveryStatus? = null,

    @CsvColumn(columnName = "Created on")
    var createdOn: ZonedDateTime? = null,

    id: Long?
) : BaseCsv(id)

