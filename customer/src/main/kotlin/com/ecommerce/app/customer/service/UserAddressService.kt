package com.ecommerce.app.customer.service

import com.ecommerce.app.commonlibrary.exception.AccessDeniedException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.customer.model.UserAddress
import com.ecommerce.app.customer.repository.UserAddressRepository
import com.ecommerce.app.customer.utils.Constants
import com.ecommerce.app.customer.viewmodel.address.ActiveAddressVm
import com.ecommerce.app.customer.viewmodel.address.AddressDetailVm
import com.ecommerce.app.customer.viewmodel.address.AddressPostVm
import com.ecommerce.app.customer.viewmodel.useraddress.UserAddressVm
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional
class UserAddressService(
    private val userAddressRepository: UserAddressRepository,
    private val locationService: LocationService
) {

    fun getUserAddressList(): List<ActiveAddressVm> {
        val userId = SecurityContextHolder.getContext().authentication.name
        if (userId.equals("anonymousUser")) {
            throw AccessDeniedException(Constants.ErrorCode.UNAUTHENTICATED)
        }
        val userAddressList = userAddressRepository.findAllByUserId(userId)
        val addressVmList: List<AddressDetailVm>? = locationService.getAddressesByIdList(
            userAddressList.map { it.addressId!! }
        )

        val addressActiveVms: List<ActiveAddressVm> = userAddressList.flatMap { userAddress ->
            addressVmList?.filter { addressDetailVm ->
                userAddress.addressId == addressDetailVm.id
            }!!.map { addressDetailVm ->
                ActiveAddressVm(
                    addressDetailVm.id,
                    addressDetailVm.contactName,
                    addressDetailVm.phone,
                    addressDetailVm.addressLine1,
                    addressDetailVm.city,
                    addressDetailVm.zipCode,
                    addressDetailVm.districtId,
                    addressDetailVm.districtName,
                    addressDetailVm.stateOrProvinceId,
                    addressDetailVm.stateOrProvinceName,
                    addressDetailVm.countryId,
                    addressDetailVm.countryName,
                    userAddress.isActive!!
                )
            }
        }

        val comparator = Comparator.comparing(ActiveAddressVm::isActive).reversed()
        return addressActiveVms.stream().sorted(comparator).collect(Collectors.toList())
    }

    fun getAddressDefault(): AddressDetailVm? {
        val userId = SecurityContextHolder.getContext().authentication.name
        if (userId.equals("anonymousUser")) {
            throw AccessDeniedException(Constants.ErrorCode.UNAUTHENTICATED)
        }
        val userAddress = userAddressRepository.findByUserIdAndIsActiveTrue(userId)
            .orElseThrow { NotFoundException(Constants.ErrorCode.USER_ADDRESS_NOT_FOUND) }

        return locationService.getAddressById(userAddress.addressId!!)
    }

    fun createAddress(addressPostVm: AddressPostVm): UserAddressVm {
        val userId = SecurityContextHolder.getContext().authentication.name

        val userAddressList = userAddressRepository.findAllByUserId(userId)
        val isFirstAddress = userAddressList.isEmpty()
        val addressGetVm = locationService.createAddress(addressPostVm)
        val userAddress  = UserAddress(userId = userId, addressId = addressGetVm?.id, isActive = isFirstAddress)
        return UserAddressVm.fromModel(userAddressRepository.save(userAddress), addressGetVm)
    }

    fun deleteAddress(id: Long) {
        val userId = SecurityContextHolder.getContext().authentication.name
        val userAddress = userAddressRepository.findOneByUserIdAndAddressId(userId, id)
        if (userAddress == null) {
            throw NotFoundException(Constants.ErrorCode.USER_ADDRESS_NOT_FOUND)
        }
        userAddressRepository.delete(userAddress)
    }

    fun chooseDefaultAddress(id: Long) {
        val userId = SecurityContextHolder.getContext().authentication.name
        val userAddressList = userAddressRepository.findAllByUserId(userId)
        for (userAddress in userAddressList) {
            userAddress.isActive = userAddress.addressId == id
        }
        userAddressRepository.saveAll(userAddressList)
    }
}
