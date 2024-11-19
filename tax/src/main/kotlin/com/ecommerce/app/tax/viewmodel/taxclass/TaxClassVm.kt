package com.ecommerce.app.tax.viewmodel.taxclass

import com.ecommerce.app.tax.model.TaxClass

data class TaxClassVm(
    val id: Long?,
    val name: String?
) {

    companion object {
        fun fromModel(taxClass: TaxClass): TaxClassVm {
            return TaxClassVm(taxClass.id, taxClass.name)
        }
    }
}
