package com.ecommerce.app.product.service

import com.ecommerce.app.product.repository.ProductAttributeGroupRepository
import com.ecommerce.app.product.repository.ProductAttributeRepository
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeGetVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeListGetVm
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductAttributeService(
    private val productAttributeRepository: ProductAttributeRepository,
    private val productAttributeGroupRepository: ProductAttributeGroupRepository
) {

    fun getPageableProductAttributes(pageNo: Int, pageSize: Int): ProductAttributeListGetVm {
        val productAttributeGetVms = mutableListOf<ProductAttributeGetVm>()
        val pageable = PageRequest.of(pageNo, pageSize)
        val productAttributePage = productAttributeRepository.findAll(pageable)
        val productAttributes = productAttributePage.content
        for (productAttribute in productAttributes) {
            productAttributeGetVms.add(ProductAttributeGetVm.fromModel(productAttribute))
        }
        return ProductAttributeListGetVm(
            productAttributeGetVms,
            productAttributePage.number,
            productAttributePage.size,
            productAttributePage.totalElements.toInt(),
            productAttributePage.totalPages,
            productAttributePage.isLast
        )
    }
}
