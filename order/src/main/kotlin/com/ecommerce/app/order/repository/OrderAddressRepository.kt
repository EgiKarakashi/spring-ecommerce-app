package com.ecommerce.app.order.repository

import com.ecommerce.app.order.model.OrderAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderAddressRepository: JpaRepository<OrderAddress, Long> {
}
