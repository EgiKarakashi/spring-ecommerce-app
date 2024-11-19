package com.ecommerce.app.order.repository

import com.ecommerce.app.order.model.Checkout
import com.ecommerce.app.order.model.enumeration.CheckoutState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CheckoutRepository: JpaRepository<Checkout, String> {

    fun findByIdAndCheckoutState(id: String, state: CheckoutState): Optional<Checkout>
}
