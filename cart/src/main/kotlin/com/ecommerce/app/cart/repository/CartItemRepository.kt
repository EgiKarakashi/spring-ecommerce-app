package com.ecommerce.app.cart.repository

import com.ecommerce.app.cart.model.CartItem
import com.ecommerce.app.cart.model.CartItemId
import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import java.util.*

interface CartItemRepository: JpaRepository<CartItem, CartItemId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "jakarta.persistence.lock.timeout", value = "0"))
    @Query("SELECT c FROM CartItem c WHERE c.customerId = :customerId AND c.productId = :productId")
    fun findByCustomerIdAndProductId(customerId: String, productId: Long): Optional<CartItem>

    fun findByCustomerIdOrderByCreatedOnDesc(customerId: String): List<CartItem>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "jakarta.persistence.lock.timeout", value = "0"))
    @Query("SELECT c FROM CartItem c WHERE c.customerId = :customerId AND c.productId IN :productIds")
    fun findByCustomerIdAndProductIdIn(customerId: String, productIds: List<Long>): List<CartItem>

    fun deleteByCustomerIdAndProductId(customerId: String, productId: Long): Unit
}
