package com.ecommerce.app.product.controller

import com.ecommerce.app.product.repository.ProductOptionRepository
import com.ecommerce.app.product.service.ProductOptionService
import com.ecommerce.app.product.viewmodel.productoption.ProductOptionGetVm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductOptionController(
    val productOptionRepository: ProductOptionRepository,
    val productOptionService: ProductOptionService
) {

    @GetMapping("/backoffice/product-options")
    fun listProductOption(): ResponseEntity<List<ProductOptionGetVm>> {
        val productOptionsGetVms = productOptionRepository
            .findAll().stream()
            .map { ProductOptionGetVm.fromModel(it) }
            .toList()
        return ResponseEntity.ok(productOptionsGetVms)
    }
}
