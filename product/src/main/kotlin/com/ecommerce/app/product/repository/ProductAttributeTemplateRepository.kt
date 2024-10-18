package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.attribute.ProductAttributeTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductAttributeTemplateRepository: JpaRepository<ProductAttributeTemplate, Long> {

    fun findAllByProductTemplateId(productTemplateId: Long): List<ProductAttributeTemplate>
}
