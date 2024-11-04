package com.ecommerce.app.customer.viewmodel.useraddress

import com.ecommerce.app.customer.model.UserAddress
import com.ecommerce.app.customer.viewmodel.address.AddressVm

data class UserAddressVm(
    val id: Long?,
    val userId: String?,
    val addressGetVm: AddressVm?,
    val isActive: Boolean?
) {
    companion object{
        fun fromModel(userAddress: UserAddress, addressGetVm: AddressVm?): UserAddressVm {
            return UserAddressVm(
                id = userAddress.id,
                userId = userAddress.userId,
                addressGetVm,
                isActive = userAddress.isActive
            )
        }
    }
}


