package com.ecommerce.app.product.service

import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.product.model.Product
import com.ecommerce.app.product.repository.ProductOptionCombinationRepository
import com.ecommerce.app.product.repository.ProductRepository
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.ImageVm
import com.ecommerce.app.product.viewmodel.product.ProductDetailInfoVm
import com.ecommerce.app.product.viewmodel.product.ProductVariationGetVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeValueGetVm
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Collections
import java.util.Optional

@Slf4j
@Service
@Transactional
class ProductDetailService(
    private val productRepository: ProductRepository,
    private val mediaService: MediaService,
    private val productOptionCombinationRepository: ProductOptionCombinationRepository,
) {

    fun getProductDetailById(productId: Long): ProductDetailInfoVm {
        val variations = mutableListOf<ProductVariationGetVm>()
        val product = productRepository
            .findById(productId)
            .filter { it.isPublished == true }
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, productId) }

        val categories = Optional.ofNullable(product.productCategories)
            .orElse(Collections.emptyList())
            .stream()
            .map { it.category }
            .toList()

        val brandId = Optional.ofNullable(product.brand)
            .map { it.id }
            .orElse(null)

        val brandName = Optional.ofNullable(product.brand)
            .map { it.name }
            .orElse(null)

        val productAttributes: List<ProductAttributeValueGetVm> = product.attributeValues
            ?.map { ProductAttributeValueGetVm.fromModel(it) }
            ?: emptyList()

        if (product.hasOptions == true) {
            val productVariations = product.products?.toList() ?: emptyList()

            val variations = productVariations
                .filter { it.isPublished == true }
                .map { pro ->
                    val productOptionCombinations = productOptionCombinationRepository.findAllByProduct(pro)
                    val options = productOptionCombinations.associate {
                        it.productOption?.id to it.value
                    }

                    ProductVariationGetVm(
                        pro.id,
                        pro.name,
                        pro.slug,
                        pro.sku,
                        pro.gtin,
                        pro.price,
                        getThumbnailFromProduct(pro),
                        getImagesFromProduct(pro),
                        options
                    )
                }
        }

        return ProductDetailInfoVm(
            product.id,
            product.name,
            product.shortDescription,
            product.description,
            product.specification,
            product.sku,
            product.gtin,
            product.slug,
            product.isAllowedToOrder,
            product.isPublished,
            product.isFeatured,
            product.isVisibleIndividually,
            product.stockTrackingEnabled,
            product.price,
            brandId,
            categories,
            product.metaTitle,
            product.metaDescription,
            product.metaKeyword,
            product.taxClassId,
            brandName,
            productAttributes,
            variations,
            getThumbnailFromProduct(product),
            getImagesFromProduct(product)
        )
    }

    private fun getThumbnailFromProduct(product: Product): ImageVm {
        return Optional.ofNullable(product.thumbnailMediaId)
            .map { thumbnailId -> mediaService.getMedia(thumbnailId)?.url?.let { ImageVm(thumbnailId, it) } }
            .orElse(null)
    }

    private fun getImagesFromProduct(product: Product): List<ImageVm> {
        return Optional.ofNullable(product.productImages)
            .orElse(Collections.emptyList())
            .stream()
            .map { image -> mediaService.getMedia(image.imageId)?.url?.let { ImageVm(image.imageId, it) } }
            .toList()
    }
}
