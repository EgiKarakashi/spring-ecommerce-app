package com.ecommerce.app.springecommerceapp.vector.product.query

import com.ecommerce.app.springecommerceapp.vector.common.query.VectorQuery
import com.ecommerce.app.springecommerceapp.vector.product.document.ProductDocument
import com.ecommerce.app.springecommerceapp.viewmodel.RelatedProductVm
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class RelatedProductQuery(
    vectorStore: VectorStore
) : VectorQuery<ProductDocument, RelatedProductVm>(ProductDocument::class.java, RelatedProductVm::class.java)
