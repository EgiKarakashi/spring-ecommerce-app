package com.ecommerce.app.sampledata.controller

import com.ecommerce.app.sampledata.service.SampleDataService
import com.ecommerce.app.sampledata.viewmodel.ErrorVm
import com.ecommerce.app.sampledata.viewmodel.SampleDataVm
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class SampleDataController(
    val sampleDataService: SampleDataService
) {

    @PostMapping("/storefront/sampledata")
    @Operation(summary = "Add product to shopping sampleData. When no sampleData exists, this will create a new sampleData.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created",
            content = [Content(schema = Schema(implementation = SampleDataVm::class))]
        ),
        ApiResponse(responseCode = "400", description = "Bad request",
            content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ])
    fun createSampleData(@RequestBody @Valid sampleDataVm: SampleDataVm): SampleDataVm {
        return sampleDataService.createSampleData()
    }
}
