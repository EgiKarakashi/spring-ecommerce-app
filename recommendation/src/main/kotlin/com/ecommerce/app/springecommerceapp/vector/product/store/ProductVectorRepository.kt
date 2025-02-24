package com.ecommerce.app.springecommerceapp.vector.product.store

import com.ecommerce.app.springecommerceapp.service.ProductService
import com.ecommerce.app.springecommerceapp.vector.common.store.SimpleVectorRepository
import com.ecommerce.app.springecommerceapp.vector.product.document.ProductDocument
import com.ecommerce.app.springecommerceapp.viewmodel.ProductDetailVm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProductVectorRepository: SimpleVectorRepository<ProductDocument, ProductDetailVm>() {

    @Autowired
    private lateinit var productService: ProductService

    override fun getEntity(entityId: Long): ProductDetailVm? {
        return productService.getProductDetail(entityId)
    }
}
