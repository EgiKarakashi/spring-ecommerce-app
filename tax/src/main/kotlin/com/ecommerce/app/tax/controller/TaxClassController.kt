package com.ecommerce.app.tax.controller

import com.ecommerce.app.commonlibrary.constants.PageableConstant
import com.ecommerce.app.tax.constants.ApiConstant
import com.ecommerce.app.tax.service.TaxClassService
import com.ecommerce.app.tax.viewmodel.error.ErrorVm
import com.ecommerce.app.tax.viewmodel.taxclass.TaxClassListGetVm
import com.ecommerce.app.tax.viewmodel.taxclass.TaxClassVm
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiConstant.TAX_CLASS_URL)
class TaxClassController(
    val taxClassService: TaxClassService
) {

    @GetMapping("/paging")
    fun getPageableTaxClasses(
        @RequestParam(value = "pageNo", defaultValue = PageableConstant.DEFAULT_PAGE_NUMBER, required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = PageableConstant.DEFAULT_PAGE_SIZE, required = false) pageSize: Int
    ): ResponseEntity<TaxClassListGetVm> {
        return ResponseEntity.ok(taxClassService.getPageableTaxClasses(pageNo, pageSize))
    }

    @GetMapping
    fun listTaxClasses(): ResponseEntity<List<TaxClassVm>> {
        return ResponseEntity.ok(taxClassService.findAllTaxClasses())
    }

    @GetMapping("/{id}")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = ApiConstant.CODE_200, description = ApiConstant.OK, content = [Content(schema = Schema(implementation = TaxClassVm::class))]),
            ApiResponse(responseCode = ApiConstant.CODE_404, description = ApiConstant.OK, content = [Content(schema = Schema(implementation = ErrorVm::class))]),
        ]
    )
    fun getTaxClass(@PathVariable("id") id: Long): ResponseEntity<TaxClassVm> {
        return ResponseEntity.ok(taxClassService.findById(id))
    }
}
