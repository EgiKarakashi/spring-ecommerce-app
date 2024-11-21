package com.ecommerce.app.product.service

import com.ecommerce.app.commonlibrary.exception.BadRequestException
import com.ecommerce.app.commonlibrary.exception.DuplicatedException
import com.ecommerce.app.product.model.attribute.ProductAttribute
import com.ecommerce.app.product.repository.ProductAttributeGroupRepository
import com.ecommerce.app.product.repository.ProductAttributeRepository
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeGetVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeListGetVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributePostVm
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

    fun save(productAttributePostVm: ProductAttributePostVm): ProductAttribute {
        productAttributePostVm.name?.let { validateExistingName(it, null) }
        val productAttribute = ProductAttribute()
        productAttribute.name = productAttributePostVm.name

        if (productAttributePostVm.productAttributeGroupId != null) {
            val productAttributeGroup = productAttributeGroupRepository
                .findById(productAttributePostVm.productAttributeGroupId)
                .orElseThrow { BadRequestException(Constants.ErrorCode.PRODUCT_ATTRIBUTE_GROUP_NOT_FOUND,
                    productAttributePostVm.productAttributeGroupId) }
            productAttribute.productAttributeGroup = productAttributeGroup
        }

    return productAttributeRepository.save(productAttribute)
    }

    private fun validateExistingName(name: String, id: Long?) {
        if (checkExistingName(name, id)) {
            throw DuplicatedException(Constants.ErrorCode.NAME_ALREADY_EXITED, name)
        }
    }

    private fun checkExistingName(name: String, id: Long?): Boolean {
        return productAttributeRepository.findExistedName(name, id) != null
    }
}
