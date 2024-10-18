package com.ecommerce.app.product.controller

import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.product.constants.PageableConstant
import com.ecommerce.app.product.repository.BrandRepository
import com.ecommerce.app.product.service.BrandService
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.brand.BrandListGetVm
import com.ecommerce.app.product.viewmodel.brand.BrandPostVm
import com.ecommerce.app.product.viewmodel.brand.BrandVm
import com.ecommerce.app.product.viewmodel.error.ErrorVm
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
class BrandController(
    val brandRepository: BrandRepository,
    val brandService: BrandService
) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(BrandController::class.java)
    }

    @GetMapping("/backoffice/brands", "/storefront/brands")
    fun listBrands(
        @RequestParam(required = false, defaultValue = "") brandName: String)
    :ResponseEntity<List<BrandVm>> {
        log.info("Test logging with trace] Got a request")
        val brandVms = brandRepository.findByNameContainingIgnoreCase(brandName).stream()
            .map { BrandVm.fromModel(it) }
            .toList()
        return ResponseEntity.ok(brandVms)
    }

    @GetMapping("/backoffice/brands/paging", "storefront/brands/paging")
    fun getPageableBrands(
        @RequestParam(value = "pageNo", defaultValue = PageableConstant.DEFAULT_PAGE_NUMBER, required = false)
        pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = PageableConstant.DEFAULT_PAGE_SIZE, required = false)
        pageSize: Int
    ): ResponseEntity<BrandListGetVm> {
        return ResponseEntity.ok(brandService.getBrands(pageNo, pageSize))
    }

    @GetMapping("/backoffice/brands/{id}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Ok",
                content = [Content(schema = Schema(implementation = BrandVm::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(schema = Schema(implementation = ErrorVm::class))]
            )
        ]
    )
    fun getBrand(@PathVariable("id") id: Long): ResponseEntity<BrandVm> {
        val brand = brandRepository.findById(id)
            .orElseThrow { NotFoundException(Constants.ErrorCode.BRAND_NOT_FOUND, id) }
        return ResponseEntity.ok(BrandVm.fromModel(brand))
    }

    @PostMapping("/backoffice/brands")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created",
            content = [Content(schema = Schema(implementation = BrandVm::class))]
        ),
        ApiResponse(responseCode = "400", description = "Bad request",
            content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ])
    fun createBrand(
        @Valid @RequestBody brandPostVm: BrandPostVm,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<BrandVm> {
        val brand = brandService.create(brandPostVm)
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/brands/{id}")
            .buildAndExpand(brand.id).toUri())
            .body(BrandVm.fromModel(brand))
    }

    @PutMapping("/backoffice/brands/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "No content",
            content = [Content()]
        ),
        ApiResponse(responseCode = "404", description = "Not Found",
            content = [Content(schema = Schema(implementation = ErrorVm::class))]
        ),
        ApiResponse(responseCode = "400", description = "Bad request",
            content = [Content(schema = Schema(implementation = ErrorVm::class))]
        ),
    ])
    fun updateBrand(@PathVariable id: Long, @Valid @RequestBody brandPostVm: BrandPostVm): ResponseEntity<Unit> {
        brandService.update(brandPostVm, id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/backoffice/brands/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "No content",
            content = [Content()]
        ),
        ApiResponse(responseCode = "404", description = "Not Found",
            content = [Content(schema = Schema(implementation = ErrorVm::class))]
        ),
        ApiResponse(responseCode = "400", description = "Bad request",
            content = [Content(schema = Schema(implementation = ErrorVm::class))]
        ),
    ])
    fun deleteBrand(@PathVariable id: Long): ResponseEntity<Unit> {
        brandService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/backoffice/brands/by-ids")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "No content",
            content = [Content()]
        ),
        ApiResponse(responseCode = "404", description = "Not Found",
            content = [Content(schema = Schema(implementation = ErrorVm::class))]
        )
    ])
    fun getBrandsByIds(@RequestParam ids: List<Long>): ResponseEntity<List<BrandVm>> {
        return ResponseEntity.ok(brandService.getBrandsByIds(ids))
    }
}
