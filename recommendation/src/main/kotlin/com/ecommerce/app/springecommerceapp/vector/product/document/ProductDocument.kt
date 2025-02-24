package com.ecommerce.app.springecommerceapp.vector.product.document

import com.ecommerce.app.springecommerceapp.vector.common.document.BaseDocument
import com.ecommerce.app.springecommerceapp.vector.common.document.DocumentMetadata
import com.ecommerce.app.springecommerceapp.vector.product.formatter.ProductDocumentFormatter

@DocumentMetadata(
    docIdPrefix = ProductDocument.PREFIX_PRODUCT,
    contentFormat = ProductDocument.CONTENT_FORMAT,
    documentFormatter = ProductDocumentFormatter::class
)
class ProductDocument : BaseDocument() {

    companion object {
        const val PREFIX_PRODUCT = "PRODUCT"
        const val CONTENT_FORMAT = "{name}| {shortDescription}| {specification}| Price: {price}| {brandName}| {categories}| {metaTitle}" +
                "| {metaKeyword}| {metaDescription}| {attributeValues}"
    }
}
