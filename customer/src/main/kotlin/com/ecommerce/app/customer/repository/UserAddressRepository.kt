package com.ecommerce.app.customer.repository

import com.ecommerce.app.customer.model.UserAddress
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserAddressRepository: JpaRepository<UserAddress, Long> {
    fun findAllByUserId(userId: String): List<UserAddress>
    fun findOneByUserIdAndAddressId(userId: String, id: Long): UserAddress
    fun findByUserIdAndIsActiveTrue(userId: String): Optional<UserAddress>
}
