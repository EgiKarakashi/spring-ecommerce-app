package com.ecommerce.app.search.controller

import com.ecommerce.app.search.constant.enums.SortType
import com.ecommerce.app.search.model.ProductCriteriaDto
import com.ecommerce.app.search.service.ProductService
import com.ecommerce.app.search.viewmodel.ProductListGetVm
import com.ecommerce.app.search.viewmodel.ProductNameListVm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService
) {

    @GetMapping("/storefront/catalog-search")
    fun findProductAdvance(
        @RequestParam(defaultValue = "") keyword: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "12") size: Int,
        @RequestParam(required = false) brand: String?,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) attribute: String?,
        @RequestParam(required = false) minPrice: Double?,
        @RequestParam(required = false) maxPrice: Double?,
        @RequestParam(defaultValue = "DEFAULT") sortType: SortType
    ): ResponseEntity<ProductListGetVm> {
        val productCriteriaDto = ProductCriteriaDto(
            keyword, page, size, brand, category, attribute, minPrice, maxPrice, sortType
        )
        return ResponseEntity.ok(productService.findProductAdvance(productCriteriaDto))
    }

    @GetMapping("/storefront/search_suggest")
    fun productSearchAutoComplete(@RequestParam keyword: String): ResponseEntity<ProductNameListVm> {
        return ResponseEntity.ok(productService.autoCompleteProductName(keyword))
    }
}
