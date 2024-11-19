package com.ecommerce.app.tax.controller

import com.ecommerce.app.tax.constants.ApiConstant
import com.ecommerce.app.tax.service.TaxClassService
import com.ecommerce.app.tax.viewmodel.taxclass.TaxClassVm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiConstant.TAX_CLASS_URL)
class TaxClassController(
    val taxClassService: TaxClassService
) {

    @GetMapping
    fun listTaxClasses(): ResponseEntity<List<TaxClassVm>> {
        return ResponseEntity.ok(taxClassService.findAllTaxClasses())
    }
}
