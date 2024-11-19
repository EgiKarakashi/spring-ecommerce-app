package com.ecommerce.app.tax.service

import com.ecommerce.app.tax.repository.TaxClassRepository
import com.ecommerce.app.tax.viewmodel.taxclass.TaxClassVm
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
}
