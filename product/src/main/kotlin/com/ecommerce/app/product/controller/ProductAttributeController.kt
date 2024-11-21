package com.ecommerce.app.product.controller

import com.ecommerce.app.commonlibrary.exception.BadRequestException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.product.constants.PageableConstant
import com.ecommerce.app.product.repository.ProductAttributeRepository
import com.ecommerce.app.product.service.ProductAttributeService
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.error.ErrorVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeGetVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeListGetVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributePostVm
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.security.Principal

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

    @GetMapping("/backoffice/product-attribute/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = ProductAttributeGetVm::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ]
    )
    fun getProductAttribute(@PathVariable("id") id: Long): ResponseEntity<ProductAttributeGetVm> {
        val productAttribute = productAttributeRepository.findById(id)
            .orElseThrow { NotFoundException(String.format(Constants.ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND, id)) }
        return ResponseEntity.ok(ProductAttributeGetVm.fromModel(productAttribute))
    }

    @PostMapping("/backoffice/product-attribute")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created", content = [Content(schema = Schema(implementation = ProductAttributeGetVm::class))]),
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ]
    )
    fun createProductAttribute(@Valid @RequestBody productAttributePostVm: ProductAttributePostVm,
                               uriComponentsBuilder: UriComponentsBuilder,
                               principal: Principal): ResponseEntity<ProductAttributeGetVm> {
        val savedProductAttribute = productAttributeService.save(productAttributePostVm)
        val productAttributeGetVm = ProductAttributeGetVm.fromModel(savedProductAttribute)
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/product-attribute/{id}")
            .buildAndExpand(savedProductAttribute.id).toUri())
            .body(productAttributeGetVm)

    }

    @DeleteMapping("/backoffice/product-attribute/{id}")
    fun deleteProductAttribute(@PathVariable id: Long): ResponseEntity<Any> {
        val productAttribute = productAttributeRepository
            .findById(id)
            .orElseThrow { NotFoundException(String.format(Constants.ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND, id)) }
        if (productAttribute.attributeValues?.isNotEmpty() == true) {
    throw BadRequestException(Constants.ErrorCode.THIS_PROD_ATTRI_NOT_EXIST_IN_ANY_PROD_ATTRI)
}
        productAttributeRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
