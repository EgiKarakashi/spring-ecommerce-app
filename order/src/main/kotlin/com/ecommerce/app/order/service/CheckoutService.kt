package com.ecommerce.app.order.service

import com.ecommerce.app.commonlibrary.exception.Forbidden
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.order.mapper.CheckoutMapper
import com.ecommerce.app.order.model.Checkout
import com.ecommerce.app.order.model.enumeration.CheckoutState
import com.ecommerce.app.order.repository.CheckoutItemRepository
import com.ecommerce.app.order.repository.CheckoutRepository
import com.ecommerce.app.order.utils.AuthenticationUtils
import com.ecommerce.app.order.utils.Constants
import com.ecommerce.app.order.utils.Constants.ErrorCode.CHECKOUT_NOT_FOUND
import com.ecommerce.app.order.viewmodel.checkout.CheckoutPostVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutStatusPutVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutVm
import jakarta.transaction.Transactional
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import java.util.UUID
import java.util.stream.Collectors

@Service
@Transactional
@AllArgsConstructor
class CheckoutService(
    val checkoutRepository: CheckoutRepository,
    val checkoutItemRepository: CheckoutItemRepository,
    val orderService: OrderService,
    val checkoutMapper: CheckoutMapper
) {
    fun createCheckout(checkoutPostVm: CheckoutPostVm): CheckoutVm? {
        val checkout = checkoutMapper.toModel(checkoutPostVm)
        checkout?.checkoutState = CheckoutState.PENDING
        checkout?.id = UUID.randomUUID().toString()

        val savedCheckout  = checkoutRepository.save(checkout!!)
        val checkoutVm = checkoutMapper.toVm(savedCheckout)
        if (CollectionUtils.isEmpty(checkoutPostVm.checkoutItemPostVms)) {
            return checkoutVm
        }

        val checkoutItemList = checkoutPostVm.checkoutItemPostVms
            .map { checkoutItemPostVm ->
                checkoutMapper.toModel(checkoutItemPostVm)?.apply {
                    checkoutId = savedCheckout.id
                }
            }

        val savedCheckoutItems = checkoutItemRepository.saveAll(checkoutItemList)
        val checkoutItemVms = savedCheckoutItems
            .stream()
            .map { checkoutMapper.toVm(it) }
            .collect(Collectors.toSet()) as List<Checkout>

        val updatedCheckoutVm = checkoutVm?.copy(checkoutItemVms = checkoutItemVms)
        return updatedCheckoutVm

    }

    fun updateCheckoutStatus(checkoutStatusPutVm: CheckoutStatusPutVm): Long? {
        val checkout = checkoutRepository.findById(checkoutStatusPutVm.checkoutId)
            .orElseThrow { NotFoundException(CHECKOUT_NOT_FOUND, checkoutStatusPutVm.checkoutId) }

        checkout.checkoutState = CheckoutState.valueOf(checkoutStatusPutVm.checkoutStatus)
        checkoutRepository.save(checkout)

        val order = orderService.findOrderByCheckoutId(checkoutStatusPutVm.checkoutId)
        return order.id
    }

    fun getCheckoutPendingStateWithItemsById(id: String): CheckoutVm? {
        val checkout = checkoutRepository.findByIdAndCheckoutState(id, CheckoutState.PENDING).orElseThrow {
            NotFoundException(CHECKOUT_NOT_FOUND, id)
        }

        if (!checkout.createdBy.equals( AuthenticationUtils.getCurrentUserId())) {
            throw Forbidden(Constants.ErrorCode.FORBIDDEN)
        }

        val checkoutVm = checkoutMapper.toVm(checkout)

        val checkoutItems = checkoutItemRepository.findAllByCheckoutId(checkout.id!!)
        if (CollectionUtils.isEmpty(checkoutItems)) {
            return checkoutVm
        }

        val checkoutItemVms = checkoutItems
            .stream()
            .map { checkoutMapper.toVm(it) }
            .collect(Collectors.toSet()) as List<Checkout>

        val updatedCheckoutVm = checkoutVm?.copy(checkoutItemVms = checkoutItemVms)
        return updatedCheckoutVm

    }
}
