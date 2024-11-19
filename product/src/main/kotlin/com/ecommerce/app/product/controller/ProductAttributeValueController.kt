package com.ecommerce.app.product.controller

import com.ecommerce.app.commonlibrary.exception.BadRequestException
import com.ecommerce.app.product.repository.ProductAttributeRepository
import com.ecommerce.app.product.repository.ProductAttributeValueRepository
import com.ecommerce.app.product.repository.ProductRepository
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeValueGetVm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductAttributeValueController(
    val productAttributeValueRepository: ProductAttributeValueRepository,
    val productAttributeRepository: ProductAttributeRepository,
    val productRepository: ProductRepository
) {

    @GetMapping("/backoffice/product-attribute-value/{productId}")
    fun listProductAttributeValuesByProductId(@PathVariable("productId") productId: Long): ResponseEntity<List<ProductAttributeValueGetVm>> {
        val product = productRepository
            .findById(productId)
            .orElseThrow { BadRequestException(Constants.ErrorCode.PRODUCT_NOT_FOUND, productId) }
        val productAttributeValueGetVms = productAttributeValueRepository
            .findAllByProduct(product).stream()
            .map { ProductAttributeValueGetVm.fromModel(it) }
            .toList()
        return ResponseEntity.ok(productAttributeValueGetVms)
    }
}
