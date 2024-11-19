package com.ecommerce.app.order.service

import com.ecommerce.app.commonlibrary.csv.BaseCsv
import com.ecommerce.app.commonlibrary.csv.CsvExporter
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.order.mapper.OrderMapper
import com.ecommerce.app.order.model.Order
import com.ecommerce.app.order.model.OrderAddress
import com.ecommerce.app.order.model.OrderItem
import com.ecommerce.app.order.model.csv.OrderItemCsv
import com.ecommerce.app.order.model.enumeration.DeliveryStatus
import com.ecommerce.app.order.model.enumeration.OrderStatus
import com.ecommerce.app.order.model.enumeration.PaymentStatus
import com.ecommerce.app.order.model.request.OrderRequest
import com.ecommerce.app.order.repository.OrderItemRepository
import com.ecommerce.app.order.repository.OrderRepository
import com.ecommerce.app.order.specification.OrderSpecification
import com.ecommerce.app.order.utils.AuthenticationUtils
import com.ecommerce.app.order.utils.Constants
import com.ecommerce.app.order.utils.Constants.ErrorCode.ORDER_NOT_FOUND
import com.ecommerce.app.order.viewmodel.order.*
import com.ecommerce.app.order.viewmodel.promotion.PromotionUsageVm
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import java.util.stream.Collectors
import org.springframework.data.util.Pair
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

@Slf4j
@Service
@Transactional
class OrderService(
    val orderRepository: OrderRepository,
    val orderItemRepository: OrderItemRepository,
    val productService: ProductService,
    val cartService: CartService,
    val promotionService: PromotionService,
    private val orderMapper: OrderMapper
) {
    fun createOrder(orderPostVm: OrderPostVm): OrderVm {
        val billingAddressPostVm = orderPostVm.billingAddressPostVm
        val billOrderAddress = OrderAddress(
            id = null,
            phone = billingAddressPostVm.phone,
            contactName = billingAddressPostVm.contactName,
            addressLine1 = billingAddressPostVm.addressLine1,
            addressLine2 = billingAddressPostVm.addressLine2!!,
            city = billingAddressPostVm.city,
            zipCode = billingAddressPostVm.zipCode,
            districtId = billingAddressPostVm.districtId,
            districtName = billingAddressPostVm.districtName,
            stateOrProvinceId = billingAddressPostVm.stateOrProvinceId,
            stateOrProvinceName = billingAddressPostVm.stateOrProvinceName,
            countryId = billingAddressPostVm.countryId,
            countryName = billingAddressPostVm.countryName,
        )

        val shipOrderAddressPostVm = orderPostVm.shippingAddressPostVm
        val shippOrderAddress  = OrderAddress(
            id = null,
            phone = shipOrderAddressPostVm.phone,
            contactName = shipOrderAddressPostVm.contactName,
            addressLine1 = shipOrderAddressPostVm.addressLine1,
            addressLine2 = shipOrderAddressPostVm.addressLine2!!,
            city = shipOrderAddressPostVm.city,
            zipCode = shipOrderAddressPostVm.zipCode,
            districtId = shipOrderAddressPostVm.districtId,
            districtName = shipOrderAddressPostVm.districtName,
            stateOrProvinceId = shipOrderAddressPostVm.stateOrProvinceId,
            stateOrProvinceName = shipOrderAddressPostVm.stateOrProvinceName,
            countryId = shipOrderAddressPostVm.countryId,
            countryName = shipOrderAddressPostVm.countryName,
        )

        val order = Order(
            email = orderPostVm.email,
            note = orderPostVm.note,
            tax = orderPostVm.tax,
            discount = orderPostVm.discount,
            numberItem = orderPostVm.numberItem,
            totalPrice = orderPostVm.totalPrice,
            couponCode = orderPostVm.couponCode!!,
            orderStatus = OrderStatus.PENDING,
            deliveryFee = orderPostVm.deliveryFee,
            deliveryMethod = orderPostVm.deliveryMethod,
            deliveryStatus = DeliveryStatus.PREPARING,
            paymentStatus = orderPostVm.paymentStatus,
            shippingAddressId = shippOrderAddress,
            billingAddressId = billOrderAddress,
            checkoutId = orderPostVm.checkoutId
        )
        orderRepository.save(order)

        val orderItems = orderPostVm.orderItemPostVms.stream()
            .map { item -> OrderItem(
                productId = item.productId,
                productName = item.productName,
                quantity = item.quantity,
                productPrice = item.productPrice,
                note = item.note,
                orderId = order.id
            ) }
            .collect(Collectors.toSet())
        orderItemRepository.saveAll(orderItems)

        val orderVm = OrderVm.fromModel(order, orderItems)
        productService.subtractProductStockQuantity(orderVm)
        cartService.deleteCartItems(orderVm)
        acceptOrder(orderVm.id)

        val promotionUsageVms = mutableListOf<PromotionUsageVm>()
        orderItems.forEach { item ->
            val promotionUsageVm = PromotionUsageVm(
                promotionCode = order.couponCode!!,
                productId = item.productId!!,
                null,
                orderId = order.id!!
            )
            promotionUsageVms.add(promotionUsageVm)
        }
        promotionService.updateUsagePromotion(promotionUsageVms)
        return orderVm
    }

    fun acceptOrder(orderId: Long?) {
        val order = orderRepository.findById(orderId!!)
            .orElseThrow { NotFoundException(ORDER_NOT_FOUND, orderId) }
        order.orderStatus = OrderStatus.ACCEPTED
        orderRepository.save(order)
    }

    fun updateOrderPaymentStatus(paymentOrderStatusVm: PaymentOrderStatusVm): PaymentOrderStatusVm {
        val order = orderRepository.findById(paymentOrderStatusVm.orderId)
            .orElseThrow { NotFoundException(ORDER_NOT_FOUND, paymentOrderStatusVm.orderId) }
        order.paymentId = paymentOrderStatusVm.paymentId
        val paymentStatus = paymentOrderStatusVm.paymentStatus
        order.paymentStatus = PaymentStatus.valueOf(paymentStatus)
        if (PaymentStatus.COMPLETED.name == paymentStatus) {
            order.orderStatus = OrderStatus.PAID
        }
        val result = orderRepository.save(order)
        return PaymentOrderStatusVm(
            orderId = result.id!!,
            orderStatus = result.orderStatus!!.name,
            paymentId = paymentOrderStatusVm.paymentId,
            paymentStatus = paymentOrderStatusVm.paymentStatus
        )
    }

    fun isOrderCompletedWithUserIdAndProductId(productId: Long): OrderExistsByProductAndUserGetVm {
        val userId = AuthenticationUtils.getCurrentUserId()
        val productVariations = productService.getProductVariations(productId)
        val productIds: List<Long> = if (productVariations.isNullOrEmpty()) {
            listOf(productId)
        } else {
            productVariations.map { it.id }
        }
        val spec = OrderSpecification.existsByCreatedByAndInProductIdAndOrderStatusCompleted(userId, productIds)
        val existedOrder = orderRepository.findOne(spec).isPresent
        return OrderExistsByProductAndUserGetVm(existedOrder)
    }

    fun getMyOrders(productName: String, orderStatus: OrderStatus): List<OrderGetVm> {
        val userId = AuthenticationUtils.getCurrentUserId()
        val orderSpec = OrderSpecification.findMyOrders(userId, productName, orderStatus)
        val sort = Sort.by(Sort.Direction.DESC, Constants.Column.CREATE_ON_COLUMN)
        val orders = orderRepository.findAll(orderSpec, sort)
        return orders.stream().map { order -> OrderGetVm.fromModel(order, null) }.toList()
    }

    fun getOrderWithItemsById(id: Long): OrderVm {
        val order = orderRepository.findById(id).orElseThrow {
            NotFoundException(Constants.ErrorCode.ORDER_NOT_FOUND, id)
        }
        val orderItems = orderItemRepository.findAllByOrderId(order.id!!)
        return OrderVm.fromModel(order, orderItems.toSet())
    }

    fun findOrderVmByCheckoutId(checkoutId: String): OrderGetVm {
        val order: Order = findOrderByCheckoutId(checkoutId)
        val orderItems = orderItemRepository.findAllByOrderId(order.id!!)
        return OrderGetVm.fromModel(order, orderItems.toSet())
    }

    fun findOrderByCheckoutId(checkoutId: String): Order {
        return orderRepository.findByCheckoutId(checkoutId)
            .orElseThrow { NotFoundException(ORDER_NOT_FOUND, "of checkoutId $checkoutId") }
    }

    fun getAllOrder(timePair: Pair<ZonedDateTime?, ZonedDateTime?>,
                    productName: String?,
                    orderStatus: List<OrderStatus>?,
                    billingPair: Pair<String?, String?>,
                    email: String?,
                    infoPage: Pair<Int, Int>): OrderListVm {
        val sort = Sort.by(Sort.Direction.DESC, Constants.Column.CREATE_ON_COLUMN)
        val pageable = PageRequest.of(infoPage.first, infoPage.second, sort)
        val allOrderStatus = listOf(OrderStatus.entries.toTypedArray())
        val createdFrom = timePair.first
        val createdTo = timePair.second
        val billingCountry  = billingPair.first
        val billingPhoneNumber = billingPair.second

        val spec = OrderSpecification.findOrderByWithMulCriteria(
            (orderStatus?.ifEmpty { allOrderStatus } as? List<OrderStatus>) ?: emptyList(),
            billingPhoneNumber,
            billingCountry,
            email!!,
            productName!!,
            createdFrom,
            createdTo
        )

        val orderPage = orderRepository.findAll(spec, pageable)
        if (orderPage.isEmpty) {
            return OrderListVm(null, 0, 0)
        }

        val orderVms = orderPage.content
            .stream()
            .map { OrderBriefVm.fromModel(it) }
            .toList()

        return OrderListVm(orderVms, orderPage.totalElements, orderPage.totalPages)
    }

    fun getLatestOrders(count: Int): List<OrderBriefVm> {
        if (count <= 0) {
            return mutableListOf()
        }
        val pageable = PageRequest.of(0, count)
        val orders = orderRepository.getLatestOrders(pageable)
        return orders.stream()
            .map { OrderBriefVm.fromModel(it) }
            .toList()
    }

    @Throws(IOException::class)
    fun exportCsv(orderRequest: OrderRequest): ByteArray {
        val createdFrom = orderRequest.createdFrom
        val createTo = orderRequest.createdTo
        val productName = orderRequest.productName
        val orderStatus = orderRequest.orderStatus
        val billingCountry = orderRequest.billingCountry
        val billingPhoneNumber = orderRequest.billingPhoneNumber
        val email = orderRequest.email
        val pageNo = orderRequest.pageNo
        val pageSize = orderRequest.pageSize

        val orderListVm = getAllOrder(
            Pair.of(createdFrom!!, createTo!!),
            productName,
            orderStatus,
            Pair.of(billingCountry!!, billingPhoneNumber!!),
            email,
            Pair.of(pageNo, pageSize)
        )

        if (Objects.isNull(orderListVm.orderList)) {
            return CsvExporter.exportToCsv(mutableListOf(), OrderItemCsv::class.java)
        }

        val orders = orderListVm.orderList?.stream()?.map { orderMapper!!::toCsv }?.collect(
            Collectors.toUnmodifiableList()
        ) as List<BaseCsv>
        return CsvExporter.exportToCsv(orders, OrderItemCsv::class.java)
    }
}
