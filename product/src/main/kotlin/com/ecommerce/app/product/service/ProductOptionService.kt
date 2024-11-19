package com.ecommerce.app.product.service

import com.ecommerce.app.product.repository.ProductOptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductOptionService(
    val productOptionRepository: ProductOptionRepository
) {
}
