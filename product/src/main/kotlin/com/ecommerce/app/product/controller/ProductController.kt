package com.ecommerce.app.product.controller

import com.ecommerce.app.product.model.enumeration.FilterExistInWhenSelection
import com.ecommerce.app.product.service.ProductDetailService
import com.ecommerce.app.product.service.ProductService
import com.ecommerce.app.product.viewmodel.error.ErrorVm
import com.ecommerce.app.product.viewmodel.product.*
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class ProductController(
    private val productService: ProductService,
    private val productDetailService: ProductDetailService
) {

    @GetMapping("/backoffice/products")
    fun listProducts(
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) pageSize: Int,
        @RequestParam(value = "product-name", defaultValue = "", required = false) productName: String,
        @RequestParam(value = "brand-name", defaultValue = "", required = false) brandName: String
    ): ResponseEntity<ProductListGetVm> {
        return ResponseEntity.ok(productService.getProductsWithFilter(pageNo, pageSize, productName, brandName))
    }


    @GetMapping("/backoffice/export/products")
    fun exportProducts(
        @RequestParam(value = "product-name", defaultValue = "", required = false) productName: String,
        @RequestParam(value = "brand-name", defaultValue = "", required = false) brandName: String
    ): ResponseEntity<List<ProductExportingDetailVm>> {
        return ResponseEntity.ok(productService.exportProducts(productName, brandName))
    }

    @PostMapping("/backoffice/products", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created",
            content = [Content(schema = Schema(implementation = ProductGetDetailVm::class))]
        ),
        ApiResponse(responseCode = "400", description = "Bad request",
            content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ])
    fun createProduct(@Valid @RequestBody productPostVm: ProductPostVm): ResponseEntity<ProductGetDetailVm> {
        val productGetDetailVm = productService.createProduct(productPostVm)
        return ResponseEntity(productGetDetailVm, HttpStatus.CREATED)
    }

    @PutMapping("/backoffice/products/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Updated"),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = ErrorVm::class))]),
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ])
    fun updateProduct(@PathVariable id: Long, @Valid @RequestBody productPutVm: ProductPutVm): ResponseEntity<Unit> {
        productService.updateProduct(id, productPutVm)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/storefront/products/featured")
    fun getFeaturedProducts(
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) pageSize: Int
    ): ResponseEntity<ProductFeatureGetVm> {
        return ResponseEntity.ok(productService.getListFeaturedProducts(pageNo, pageSize))
    }

    @GetMapping("storefront/brand/{brandSlug}/products")
    fun getProductsByBrand(@PathVariable brandSlug: String): ResponseEntity<List<ProductThumbnailVm>> {
        return ResponseEntity.ok(productService.getProductsByBrand(brandSlug))
    }

    @GetMapping("/storefront/category/{categorySlug}/products", "/backoffice/category/{categorySlug}/products")
    fun getProductsByCategory(
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = "2", required = false) pageSize: Int,
        @PathVariable categorySlug: String
    ): ResponseEntity<ProductListGetFromCategoryVm> {
        return ResponseEntity.ok(productService.getProductsFromCategory(pageNo, pageSize, categorySlug))
    }

    @GetMapping("/backoffice/products/{productId}")
    fun getProductById(@PathVariable productId: Long): ResponseEntity<ProductDetailVm> {
        return ResponseEntity.ok(productService.getProductById(productId))
    }

    @GetMapping("/storefront/products/list-featured")
    fun getFeaturedProductsById(@RequestParam("productId") productIds: List<Long>)
        : ResponseEntity<List<ProductThumbnailGetVm>> {
        return ResponseEntity.ok(productService.getFeaturedProductsById(productIds))
    }

    @GetMapping("/storefront/product/{slug}")
    fun getProductDetail(@PathVariable("slug") slug: String): ResponseEntity<ProductDetailGetVm> {
        return ResponseEntity.ok(productService.getProductDetail(slug))
    }

    @DeleteMapping("/backoffice/products/{id}")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "No content", content = [Content()]),
            ApiResponse(responseCode = "404", description = "No found", content = [Content(schema = Schema(implementation = ErrorVm::class))]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = ErrorVm::class))])
        ]
    )
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/storefront/products")
    fun getProductsByMultiQuery(
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) pageSize: Int,
        @RequestParam(value = "productName", defaultValue = "", required = false) productName: String,
        @RequestParam(value = "categorySlug", defaultValue = "", required = false) categorySlug: String,
        @RequestParam(value = "startPrice", required = false) startPrice: Double?,
        @RequestParam(value = "endPrice", required = false) endPrice: Double?
    ): ResponseEntity<ProductsGetVm> {
        return ResponseEntity.ok(productService.getProductsByMultiQuery(
            pageNo, pageSize, productName, categorySlug, startPrice, endPrice
        ))
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description =  "Get product variations by parent id successfully",
            content = [Content(mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = ProductVariationGetVm::class)))]),
        ApiResponse(responseCode = "400", description = "Bad request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorVm::class))]),
        ApiResponse(responseCode = "404", description = "Not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorVm::class))])
    ])
    @GetMapping("/storefront/product-variations/{id}", "/backoffice/product-variations/{id}")
    fun getProductVariationsByParentId(@PathVariable id: Long): ResponseEntity<List<ProductVariationGetVm>> {
        return ResponseEntity.ok(productService.getProductVariationsByParentId(id))
    }

    @GetMapping("/storefront/productions/{id}/slug")
    fun getProductSlug(@PathVariable id: Long): ResponseEntity<ProductSlugGetVm> {
        return ResponseEntity.ok(productService.getProductSlug(id))
    }

    @GetMapping("/storefront/products-es/{productId}")
    fun getProductEsDetailById(@PathVariable productId: Long): ResponseEntity<ProductEsDetailVm> {
        return ResponseEntity.ok(productService.getProductEsDetailById(productId))
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Get related products by product id successfully",
            content = [Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = ProductVariationGetVm::class)))]),
        ApiResponse(responseCode = "404", description = "Not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorVm::class))])
    ])
    @GetMapping("/backoffice/products/related-products/{id}")
    fun getRelatedProductsBackoffice(@PathVariable id: Long): ResponseEntity<List<ProductListVm>> {
        return ResponseEntity.ok(productService.getRelatedProductsBackoffice(id))
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Get related products by product id successfully",
            content = [Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = ProductVariationGetVm::class)))]),
        ApiResponse(responseCode = "404", description = "Not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorVm::class))])
    ])
    @GetMapping("/storefront/products/related-products/{id}")
    fun getRelatedProductsStorefront(
        @PathVariable id: Long,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) pageSize: Int): ResponseEntity<ProductsGetVm> {
        return ResponseEntity.ok(productService.getRelateProductsStorefront(id, pageNo, pageSize))
    }

    @GetMapping("/backoffice/products/for-warehouse")
    fun getProductsForWarehouse(
        @RequestParam name: String,
        @RequestParam sku: String,
        @RequestParam(required = false) productIds: List<Long>?,
        @RequestParam(required = false) selection: FilterExistInWhenSelection?
    ): ResponseEntity<List<ProductInfoVm>> {
        return ResponseEntity.ok(productService.getProductsForWarehouse(name, sku, productIds, selection))
    }

    @PutMapping("/backoffice/products/update-quantity")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Updated"),
        ApiResponse(responseCode = "404", description = "Not found", content =  [Content(schema  = Schema(implementation = ErrorVm::class))]),
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = ErrorVm::class))] )
    ])
    fun updateProductQuantity(
        @Valid @RequestBody productQuantityPostVms: List<ProductQuantityPostVm>
    ): ResponseEntity<Unit> {
        productService.updateProductQuantity(productQuantityPostVms)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/backoffice/products/subtract-quantity", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Updated"),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = ErrorVm::class))]),
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ])
    fun subtractProductQuantity(
        @Valid @RequestBody productQuantityPutVm: List<ProductQuantityPutVm>
    ): ResponseEntity<Unit> {
        productService.subtractStockQuantity(productQuantityPutVm)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/backoffice/products/by-ids")
    fun getProductByIds(@RequestParam ids: List<Long>): ResponseEntity<List<ProductListVm>> {
        return ResponseEntity.ok(productService.getProductByIds(ids))
    }

    @GetMapping("/backoffice/products/by-categories")
    fun getProductByCategories(@RequestParam("ids") categoryIds: List<Long>): ResponseEntity<List<ProductListVm>> {
        return ResponseEntity.ok(productService.getProductByCategoryIds(categoryIds))
    }

    @GetMapping("/backoffice/products/by-brands")
    fun getProductByBrands(@RequestParam("ids") brandIds: List<Long>): ResponseEntity<List<ProductListVm>> {
        return ResponseEntity.ok(productService.getProductByBrandIds(brandIds))
    }

    @GetMapping("/backoffice/products/latest/{count}")
    fun getLatestProduct(@PathVariable count: Int): ResponseEntity<List<ProductListVm>> {
        return ResponseEntity.ok(productService.getLatestProduct(count))
    }

    @GetMapping("/storefront/products/detail/{productId}")
    fun getProductDetailById(@PathVariable("productId") productId: Long): ResponseEntity<ProductDetailInfoVm> {
        return ResponseEntity.ok(productDetailService.getProductDetailById(productId))
    }
}
