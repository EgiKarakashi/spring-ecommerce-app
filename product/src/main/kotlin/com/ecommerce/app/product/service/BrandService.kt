package com.ecommerce.app.product.service

import com.ecommerce.app.commonlibrary.exception.BadRequestException
import com.ecommerce.app.commonlibrary.exception.DuplicatedException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.product.model.Brand
import com.ecommerce.app.product.repository.BrandRepository
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.brand.BrandListGetVm
import com.ecommerce.app.product.viewmodel.brand.BrandPostVm
import com.ecommerce.app.product.viewmodel.brand.BrandVm
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BrandService(
    val brandRepository: BrandRepository
) {

    fun getBrands(pageNo: Int, pageSize: Int): BrandListGetVm {
        val pageable = PageRequest.of(pageNo, pageSize)
        val brandPage = brandRepository.findAll(pageable)
        val brandList = brandPage.content
        val brandVms = brandList.map { BrandVm.fromModel(it) }.toMutableList()

        return BrandListGetVm(
            brandVms,
            brandPage.number,
            brandPage.size,
            brandPage.totalElements.toInt(),
            brandPage.totalPages,
            brandPage.isLast
        )
    }

    fun create(brandPostVm: BrandPostVm): Brand {
        validateExistedName(brandPostVm.name, null)
        return brandRepository.save(brandPostVm.toModel())
    }

    fun update(brandPostVm: BrandPostVm, id: Long): Brand {
        validateExistedName(brandPostVm.name, id)

        val brand = brandRepository
            .findById(id)
            .orElseThrow { NotFoundException(Constants.ErrorCode.BRAND_NOT_FOUND, id) }
        brand.slug = brandPostVm.slug
        brand.name = brandPostVm.name
        brand.isPublished = brand.isPublished

        return brandRepository.save(brand)
    }

    private fun validateExistedName(name: String, id: Long?) {
        if (id?.let { checkExistedName(name, it) } == true) {
            throw DuplicatedException(Constants.ErrorCode.NAME_ALREADY_EXITED, name)
        }
    }

    private fun checkExistedName(name: String, id: Long): Boolean {
        return brandRepository.findExistedName(name, id) != null
    }

    fun getBrandsByIds(ids: List<Long>): List<BrandVm> {
        return brandRepository.findAllById(ids).map { BrandVm.fromModel(it) }
    }

    fun delete(id: Long) {
        val brand = brandRepository.findById(id).orElseThrow {
            NotFoundException(Constants.ErrorCode.BRAND_NOT_FOUND, id)
        }
        if (brand.products?.isEmpty() == false) {
            throw BadRequestException(Constants.ErrorCode.MAKE_SURE_BRAND_DONT_CONTAINS_ANY_PRODUCT)
        }
        brandRepository.deleteById(id)
    }
}
