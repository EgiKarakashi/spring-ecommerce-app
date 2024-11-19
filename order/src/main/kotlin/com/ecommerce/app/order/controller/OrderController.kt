package com.ecommerce.app.order.controller

import com.ecommerce.app.commonlibrary.csv.CsvExporter
import com.ecommerce.app.order.model.OrderItem
import com.ecommerce.app.order.model.csv.OrderItemCsv
import com.ecommerce.app.order.model.enumeration.OrderStatus
import com.ecommerce.app.order.model.request.OrderRequest
import com.ecommerce.app.order.service.OrderService
import com.ecommerce.app.order.viewmodel.order.*
import jakarta.validation.Valid
import org.springframework.data.util.Pair
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.time.ZonedDateTime

@RestController
class OrderController(
    val orderService: OrderService
) {

    @PostMapping("/storefront/orders")
    fun createOrder(@Valid @RequestBody orderPostVm: OrderPostVm): ResponseEntity<OrderVm> {
    val orderVm = orderService.createOrder(orderPostVm)
        return ResponseEntity.ok(orderVm)
    }

    @PutMapping("/storefront/orders/status")
    fun updateOrderPaymentStatus(
        @Valid @RequestBody paymentOrderStatusVm: PaymentOrderStatusVm
    ): ResponseEntity<PaymentOrderStatusVm> {
        val orderStatusVm = orderService.updateOrderPaymentStatus(paymentOrderStatusVm)
        return ResponseEntity.ok(orderStatusVm)
    }

    @GetMapping("/storefront/orders/completed")
    fun checkOrderExistsByProductIdAndUserIdWithStatus(@RequestParam productId: Long):
            ResponseEntity<OrderExistsByProductAndUserGetVm> {
        return ResponseEntity.ok(orderService.isOrderCompletedWithUserIdAndProductId(productId))
    }

    @GetMapping("/storefront/orders/my-orders")
    fun getMyOrders(@RequestParam productName: String,
                    @RequestParam(required = false) orderStatus: OrderStatus): ResponseEntity<List<OrderGetVm>> {
        return ResponseEntity.ok(orderService.getMyOrders(productName, orderStatus))
    }

    @GetMapping("/backoffice/orders/{id}")
    fun getOrderWithItemsById(@PathVariable id: Long): ResponseEntity<OrderVm> {
        return ResponseEntity.ok(orderService.getOrderWithItemsById(id))
    }

    @GetMapping("/storefront/orders/checkout/{id}")
    fun getOrderWithCheckoutId(@PathVariable id: String): ResponseEntity<OrderGetVm> {
        return ResponseEntity.ok(orderService.findOrderVmByCheckoutId(id))
    }

    @GetMapping("/backoffice/orders")
    fun getOrders(
        @RequestParam(value = "createdFrom", defaultValue = "1970-01-01T00:00:00Z", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) createdFrom: ZonedDateTime,
        @RequestParam(value = "createdTo", defaultValue = "1970-01-01T00:00:00Z", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) createdTo: ZonedDateTime,
        @RequestParam(value = "productName", defaultValue = "", required = false) productName: String,
        @RequestParam(value = "orderStatus", defaultValue = "", required = false) orderStatus: List<OrderStatus>,
        @RequestParam(value = "billingPhoneNumber", defaultValue = "", required = false) billingPhoneNumber: String,
        @RequestParam(value = "email", defaultValue = "", required = false) email: String,
        @RequestParam(value = "billingCountry", defaultValue = "", required = false) billingCountry: String,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) pageSize: Int
    ): ResponseEntity<OrderListVm> {
        return ResponseEntity.ok(orderService.getAllOrder(
            Pair.of(createdFrom, createdTo),
            productName,
            orderStatus,
            Pair.of(billingCountry, billingPhoneNumber),
            email,
            Pair.of(pageNo, pageSize)
        ))
    }

    @GetMapping("/backoffice/orders/latest/{count}")
    fun getLatestOrders(@PathVariable count: Int): ResponseEntity<List<OrderBriefVm>> {
        return ResponseEntity.ok(orderService.getLatestOrders(count))
    }

    @PostMapping("/backoffice/orders/csv")
    fun exportCsv(@RequestBody orderRequest: OrderRequest): ResponseEntity<ByteArray> {
        val headers = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${CsvExporter.createFileName(OrderItemCsv::class.java)}")
            contentType = MediaType.APPLICATION_OCTET_STREAM
        }
        val csvBytes = orderService.exportCsv(orderRequest)
        return ResponseEntity.ok()
            .headers(headers)
            .body(csvBytes)
    }

    @GetMapping("/hello")
    fun getHello(): String {
        return "hello"
    }
}
