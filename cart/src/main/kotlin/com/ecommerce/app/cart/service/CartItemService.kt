package com.ecommerce.app.cart.service

import com.ecommerce.app.cart.mapper.CartItemMapper
import com.ecommerce.app.cart.model.CartItem
import com.ecommerce.app.cart.repository.CartItemRepository
import com.ecommerce.app.cart.utils.Constants
import com.ecommerce.app.cart.viewmodel.CartItemGetVm
import com.ecommerce.app.cart.viewmodel.CartItemPostVm
import com.ecommerce.app.cart.viewmodel.CartItemPutVm
import com.ecommerce.app.commonlibrary.exception.InternalServerErrorException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.commonlibrary.utils.AuthenticationUtils
import org.slf4j.LoggerFactory
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartItemService(
    private val cartItemRepository: CartItemRepository,
    private val productService: ProductService,
    private val cartItemMapper: CartItemMapper
) {

    @Transactional
    fun addCartItem(cartItemPostVm: CartItemPostVm): CartItemGetVm {
        validateProduct(cartItemPostVm.productId)
        val currentUserId = AuthenticationUtils.extractUserId()
        val cartItem = performAddCartItem(cartItemPostVm, currentUserId)

        return cartItemMapper.toGetVm(cartItem)
    }

    @Transactional
    fun updateCartItem(productId: Long, cartItemPutVm: CartItemPutVm): CartItemGetVm {
        validateProduct(productId)

        val currentUserId = AuthenticationUtils.extractUserId()
        val cartItem = cartItemMapper.toCartItem(currentUserId, productId, cartItemPutVm.quantity)
        val savedCartItem = cartItemRepository.save(cartItem)
        return cartItemMapper.toGetVm(savedCartItem)
    }

    fun getCartItems(): List<CartItemGetVm> {
        val currentUserId = AuthenticationUtils.extractUserId()
        val cartItems = cartItemRepository.findByCustomerIdOrderByCreatedOnDesc(currentUserId)
        return cartItemMapper.toGetVms(cartItems)
    }

    private fun validateProduct(productId: Long) {
        if (!productService.existsById(productId)) {
            throw NotFoundException(Constants.ErrorCode.NOT_FOUND_PRODUCT, productId)
        }
    }

    private fun performAddCartItem(cartItemPostVm: CartItemPostVm, currentUserId: String): CartItem? {
        return try {
             cartItemRepository.findByCustomerIdAndProductId(currentUserId, cartItemPostVm.productId)
                .map { existingCartItem -> updateExistingCartItem(cartItemPostVm, existingCartItem) }
                .orElseGet { createNewCartItem(cartItemPostVm, currentUserId) }
        } catch (ex: PessimisticLockingFailureException) {
            log.error("Failed to acquire lock for adding cart item $ex")
            throw InternalServerErrorException(Constants.ErrorCode.ADD_CART_ITEM_FAILED)
        }
    }

    private fun createNewCartItem(cartItemPostVm: CartItemPostVm, currentUserId: String): CartItem {
        val cartItem = cartItemMapper.toCartItem(cartItemPostVm, currentUserId)
        return cartItemRepository.save(cartItem)
    }

    private fun updateExistingCartItem(cartItemPostVm: CartItemPostVm, existingCartItem: CartItem): CartItem {
        existingCartItem.quantity = existingCartItem.quantity?.plus(cartItemPostVm.quantity)
        return cartItemRepository.save(existingCartItem)
    }

    companion object {
        private val log = LoggerFactory.getLogger(CartItemService::class.java)
    }
}
