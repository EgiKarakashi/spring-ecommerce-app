package com.ecommerce.app.order.repository

import com.ecommerce.app.order.model.Order
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface OrderRepository: JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    fun findByCheckoutId(checkoutId: String): Optional<Order>

    @Query("SELECT o FROM Order o ORDER BY o.createdOn DESC")
    fun getLatestOrders(pageable: Pageable): List<Order>
}
