package com.ecommerce.app.search.service

import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.search.config.ServiceUrlConfig
import com.ecommerce.app.search.constant.MessageCode
import com.ecommerce.app.search.model.Product
import com.ecommerce.app.search.repository.ProductRepository
import com.ecommerce.app.search.viewmodel.ProductEsDetailVm
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class ProductSyncDataService(
    val restClient: RestClient,
    val serviceUrlConfig: ServiceUrlConfig,
    val productRepository: ProductRepository
) {

    fun getProductEsDetailById(id: Long): ProductEsDetailVm? {
        val url: URI = UriComponentsBuilder.fromHttpUrl(
                serviceUrlConfig.product!!)
            .path("/storefront/product-es/{id}")
            .buildAndExpand(id).
            toUri()

        return restClient.get().uri(url).retrieve().body(ProductEsDetailVm::class.java)
    }

    fun updateProduct(id: Long) {
        val productEsDetailVm = getProductEsDetailById(id)
        val product = productRepository.findById(id).orElseThrow { NotFoundException(MessageCode.PRODUCT_NOT_FOUND, id) }

        if (productEsDetailVm?.isPublished != true) {
            productRepository.deleteById(id)
            return
        }

        product.name = productEsDetailVm.name
        product.slug = productEsDetailVm.slug
        product.price = productEsDetailVm.price
        product.isPublished = true
        product.isVisibleIndividually = productEsDetailVm.isVisibleIndividually
        product.isAllowedToOrder = productEsDetailVm.isAllowedToOrder
        product.isFeatured = productEsDetailVm.isFeatured
        product.thumbnailMediaId = productEsDetailVm.thumbnailMediaId
        product.brand = productEsDetailVm.brand
        product.categories = productEsDetailVm.categories
        product.attributes = productEsDetailVm.attributes
        productRepository.save(product)
    }

    fun createProduct(id: Long) {
        val productEsDetailVm = getProductEsDetailById(id)

        val product = Product(
            id = id,
            name = productEsDetailVm!!.name,
            slug = productEsDetailVm.slug,
            price = productEsDetailVm.price,
            isPublished = productEsDetailVm.isPublished,
            isVisibleIndividually = productEsDetailVm.isVisibleIndividually,
            isAllowedToOrder = productEsDetailVm.isAllowedToOrder,
            isFeatured = productEsDetailVm.isFeatured,
            thumbnailMediaId = productEsDetailVm.thumbnailMediaId,
            brand = productEsDetailVm.brand,
            categories = productEsDetailVm.categories,
            attributes = productEsDetailVm.attributes,
            null
        )
        productRepository.save(product)
    }

    fun deleteProduct(id: Long) {
        val productExisted = productRepository.existsById(id)
        if (productExisted != true) {
            throw NotFoundException(MessageCode.PRODUCT_NOT_FOUND, id)
        }
        productRepository.deleteById(id)
    }
}
