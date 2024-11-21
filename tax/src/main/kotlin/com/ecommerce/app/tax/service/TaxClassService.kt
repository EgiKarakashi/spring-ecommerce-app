package com.ecommerce.app.tax.service

import com.ecommerce.app.tax.constants.MessageCode
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.tax.repository.TaxClassRepository
import com.ecommerce.app.tax.viewmodel.taxclass.TaxClassListGetVm
import com.ecommerce.app.tax.viewmodel.taxclass.TaxClassVm
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TaxClassService(
    val taxClassRepository: TaxClassRepository
) {

    @Transactional(readOnly = true)
    fun findAllTaxClasses(): List<TaxClassVm> {
        return taxClassRepository
            .findAll(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map { TaxClassVm.fromModel(it) }
            .toList()
    }

    @Transactional(readOnly = true)
    fun getPageableTaxClasses(pageNo: Int, pageSize: Int): TaxClassListGetVm {
        val pageable = PageRequest.of(pageNo, pageSize)
        val taxClassPage = taxClassRepository.findAll(pageable)
        val taxClassList = taxClassPage.content

        val taxClassVms = taxClassList.stream()
            .map { TaxClassVm.fromModel(it) }
            .toList()

        return TaxClassListGetVm(
            taxClassVms,
            taxClassPage.number,
            taxClassPage.size,
            taxClassPage.totalElements.toInt(),
            taxClassPage.totalPages,
            taxClassPage.isLast
        )
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): TaxClassVm {
        val taxClass = taxClassRepository.findById(id)
            .orElseThrow { NotFoundException(MessageCode.TAX_CLASS_NOT_FOUND, id) }
        return TaxClassVm.fromModel(taxClass)
    }
}
