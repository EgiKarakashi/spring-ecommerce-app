package com.ecommerce.app.order.repository

import com.ecommerce.app.order.model.OrderItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemRepository: JpaRepository<OrderItem, Long> {

    fun findAllByOrderId(orderId: Long): List<OrderItem>
}
