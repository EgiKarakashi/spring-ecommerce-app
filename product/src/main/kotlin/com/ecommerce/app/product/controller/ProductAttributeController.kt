package com.ecommerce.app.product.controller

import com.ecommerce.app.product.constants.PageableConstant
import com.ecommerce.app.product.repository.ProductAttributeRepository
import com.ecommerce.app.product.service.ProductAttributeService
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeGetVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeListGetVm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductAttributeController(
    private val productAttributeService: ProductAttributeService,
    private val productAttributeRepository: ProductAttributeRepository
) {

    @GetMapping("/backoffice/product-attribute", "/storefront/product-attribute")
    fun listProductAttributes(): ResponseEntity<List<ProductAttributeGetVm>> {
        val productAttributeGetVms = productAttributeRepository
            .findAll()
            .map { ProductAttributeGetVm.fromModel(it) }
            .toList()
        return ResponseEntity.ok(productAttributeGetVms)
    }

    @GetMapping("/backoffice/product-attribute/paging", "/storefront/product-attribute/paging")
    fun getPageableProductAttributes(
        @RequestParam(value = "pageNo", defaultValue = PageableConstant.DEFAULT_PAGE_NUMBER, required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = PageableConstant.DEFAULT_PAGE_SIZE, required = false) pageSize: Int
    ): ResponseEntity<ProductAttributeListGetVm> {
        return ResponseEntity.ok(productAttributeService.getPageableProductAttributes(pageNo, pageSize))
    }
}
