package com.ecommerce.app.customer.controller

import com.ecommerce.app.customer.service.CustomerService
import com.ecommerce.app.customer.viewmodel.ErrorVm
import com.ecommerce.app.customer.viewmodel.customer.CustomerAdminVm
import com.ecommerce.app.customer.viewmodel.customer.CustomerListVm
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerController(
    private val customerService: CustomerService
) {

    @GetMapping("/backoffice/customers")
    @ApiResponses(value = [
        ApiResponse(responseCode = "403", description = "Access Denied",
            content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ])
    fun getCustomers(
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int
    ): ResponseEntity<CustomerListVm> {
        return ResponseEntity.ok(customerService.getCustomers(pageNo))
    }

    @GetMapping("/backoffice/customers/{email}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Ok", content = [Content(schema = Schema(implementation = CustomerAdminVm::class))]),
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content(schema = Schema(implementation = ErrorVm::class))]),
        ApiResponse(responseCode = "403", description = "Access denied", content = [Content(schema = Schema(implementation = ErrorVm::class))]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(implementation = ErrorVm::class))])
    ])
    fun getCustomerByEmail(@PathVariable email: String): ResponseEntity<CustomerAdminVm> {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email))
    }
}
