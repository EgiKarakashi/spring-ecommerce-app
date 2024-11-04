package com.ecommerce.app.customer.controller

import com.ecommerce.app.customer.service.UserAddressService
import com.ecommerce.app.customer.viewmodel.address.ActiveAddressVm
import com.ecommerce.app.customer.viewmodel.address.AddressDetailVm
import com.ecommerce.app.customer.viewmodel.address.AddressPostVm
import com.ecommerce.app.customer.viewmodel.useraddress.UserAddressVm
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserAddressController(
    private val userAddressService: UserAddressService
) {

    @GetMapping("/storefront/user-address")
    fun getUserAddress(): ResponseEntity<List<ActiveAddressVm>> {
        return ResponseEntity.ok(userAddressService.getUserAddressList())
    }

    @GetMapping("/storefront/user-address/default-address")
    fun getDefaultAddress(): ResponseEntity<AddressDetailVm> {
        return ResponseEntity.ok(userAddressService.getAddressDefault())
    }

    @PostMapping("/storefront/user-address")
    fun createAddress(@Valid @RequestBody addressPostVm: AddressPostVm): ResponseEntity<UserAddressVm> {
        return ResponseEntity.ok(userAddressService.createAddress(addressPostVm))
    }

    @DeleteMapping("/storefront/user-address/{id}")
    fun deleteAddress(@PathVariable id: Long): ResponseEntity<Void> {
        userAddressService.deleteAddress(id)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/storefront/user-address/{id}")
    fun chooseDefaultAddress(@PathVariable id: Long): ResponseEntity<Void> {
        userAddressService.chooseDefaultAddress(id)
        return ResponseEntity.ok().build()
    }
}
