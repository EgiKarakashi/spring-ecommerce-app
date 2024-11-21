package com.ecommerce.app.product.controller

import com.ecommerce.app.product.repository.ProductAttributeGroupRepository
import com.ecommerce.app.product.service.ProductAttributeGroupService
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeGroupVm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductAttributeGroupController(
    val productAttributeGroupService: ProductAttributeGroupService,
    val productAttributeGroupRepository: ProductAttributeGroupRepository
) {

    @GetMapping("/backoffice/product-attribute-groups")
    fun listProductAttributeGroups(): ResponseEntity<List<ProductAttributeGroupVm>> {
        val productAttributeGroupVms = productAttributeGroupRepository.findAll().stream()
            .map { ProductAttributeGroupVm.fromModel(it) }
            .toList()
        return ResponseEntity.ok(productAttributeGroupVms)
    }
}
