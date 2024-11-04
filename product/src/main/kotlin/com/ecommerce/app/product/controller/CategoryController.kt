package com.ecommerce.app.product.controller

import com.ecommerce.app.commonlibrary.exception.BadRequestException
import com.ecommerce.app.product.repository.CategoryRepository
import com.ecommerce.app.product.service.CategoryService
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.category.CategoryGetDetailVm
import com.ecommerce.app.product.viewmodel.category.CategoryGetVm
import com.ecommerce.app.product.viewmodel.category.CategoryPostVm
import com.ecommerce.app.product.viewmodel.error.ErrorVm
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
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.security.Principal

@RestController
class CategoryController(
    private val categoryRepository: CategoryRepository,
    private val categoryService: CategoryService
) {

    @GetMapping("/backoffice/categories", "/storefront/categories")
    fun listCategories(@RequestParam(required = false, defaultValue = "") categoryName: String): ResponseEntity<List<CategoryGetVm>> {
        return ResponseEntity.ok(categoryService.getCategories(categoryName))
    }

    @GetMapping("/backoffice/categories/{id}")
    @ApiResponses(value =  [
        ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(
            implementation = CategoryGetDetailVm::class
        ))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(
            implementation = ErrorVm::class
        ))])
    ])
    fun getCategory(@PathVariable id: Long): ResponseEntity<CategoryGetDetailVm> {
        return ResponseEntity.ok(categoryService.getCategoryById(id))
    }

    @PostMapping("/backoffice/categories")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created", content = [Content(schema = Schema(
            implementation = CategoryGetDetailVm::class
        ))]),
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(
            implementation = ErrorVm::class
        ))])
    ])
    fun createCategory(
        @Valid @RequestBody categoryPostVm: CategoryPostVm,
        uriComponentsBuilder: UriComponentsBuilder,
        principal: Principal?): ResponseEntity<CategoryGetDetailVm> {
        val savedCategory = categoryService.create(categoryPostVm)
        val categoryGetDetailVm = CategoryGetDetailVm.fromModel(savedCategory)
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/categories/{id}")
            .buildAndExpand(savedCategory.id).toUri())
            .body(categoryGetDetailVm)
    }

    @PutMapping("/backoffice/categories/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "No content"),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(
            implementation = ErrorVm::class
        ))]),
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(
            implementation = ErrorVm::class
        ))])
    ])
    fun updateCategory(
        @PathVariable id: Long,
        @RequestBody @Valid categoryPostVm: CategoryPostVm,
        principal: Principal?): ResponseEntity<Unit> {
        categoryService.update(categoryPostVm, id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/backoffice/categories/{id}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "No content"),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = ErrorVm::class))]),
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = ErrorVm::class))]),
    ])
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Unit> {
        val category = categoryRepository.findById(id)
            .orElseThrow { BadRequestException(Constants.ErrorCode.CATEGORY_NOT_FOUND, id) }
        if (!category.categories?.isEmpty()!!) {
            throw BadRequestException(Constants.ErrorCode.MAKE_SURE_CATEGORY_DO_NOT_CONTAIN_CHILDREN)
        }
        if (!category.productCategories?.isEmpty()!!) {
            throw BadRequestException(Constants.ErrorCode.MAKE_SURE_CATEGORY_DO_NOT_CONTAIN_PRODUCT)
        }
        categoryRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/backoffice/categories/by-ids")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Ok", content = [Content(schema = Schema(implementation = CategoryGetDetailVm::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ])
    fun getCategoriesById(@RequestParam ids: List<Long>): ResponseEntity<List<CategoryGetVm>> {
        return ResponseEntity.ok(categoryService.getCategoryByIds(ids))
    }
}
