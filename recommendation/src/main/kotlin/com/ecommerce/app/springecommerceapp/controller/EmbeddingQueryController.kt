package com.ecommerce.app.springecommerceapp.controller

import com.ecommerce.app.springecommerceapp.vector.common.query.VectorQuery
import com.ecommerce.app.springecommerceapp.vector.product.document.ProductDocument
import com.ecommerce.app.springecommerceapp.viewmodel.RelatedProductVm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("embedding")
class EmbeddingQueryController {

    @Autowired
    private lateinit var relatedProductSearch: VectorQuery<ProductDocument, RelatedProductVm>

    @GetMapping("/product/{id}/similarity")
    fun searchProduct(@PathVariable("id") productId: Long): List<RelatedProductVm> {
        return relatedProductSearch.similaritySearch(productId)
    }
}
