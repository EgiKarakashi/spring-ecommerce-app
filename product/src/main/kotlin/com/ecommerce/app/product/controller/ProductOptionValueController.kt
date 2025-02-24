package com.ecommerce.app.product.controller

import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.product.repository.ProductOptionValueRepository
import com.ecommerce.app.product.repository.ProductRepository
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.error.ErrorVm
import com.ecommerce.app.product.viewmodel.productoption.ProductOptionValueGetVm
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductOptionValueController(
    private val productOptionValueRepository: ProductOptionValueRepository,
    private val productRepository: ProductRepository
) {

    @GetMapping("/backoffice/product-option-values")
    fun listProductOptionsValues(): ResponseEntity<List<ProductOptionValueGetVm>> {
        val productOptionsGetVms = productOptionValueRepository
            .findAll().stream()
            .map { ProductOptionValueGetVm.fromModel(it) }
            .toList()
        return ResponseEntity.ok(productOptionsGetVms)
    }

    @GetMapping("/storefront/product-option-values/{productId}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Ok", content = [Content(schema = Schema(implementation = ProductOptionValueGetVm::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = ErrorVm::class))]),
    ])
    fun listProductOptionValueOfProduct(
        @PathVariable("productId") productId: Long
    ): ResponseEntity<List<com.ecommerce.app.product.viewmodel.product.ProductOptionValueGetVm>> {
        val product = productRepository
            .findById(productId)
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, productId) }
        val productVariations = productOptionValueRepository
            .findAllByProduct(product).stream()
            .map { com.ecommerce.app.product.viewmodel.product.ProductOptionValueGetVm.fromModel(it) }
            .toList()
        return ResponseEntity.ok(productVariations)
    }
}
