package com.ecommerce.app.product.service

import com.ecommerce.app.commonlibrary.exception.BadRequestException
import com.ecommerce.app.commonlibrary.exception.DuplicatedException
import com.ecommerce.app.commonlibrary.exception.InternalServerErrorException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.product.model.*
import com.ecommerce.app.product.model.enumeration.FilterExistInWhenSelection
import com.ecommerce.app.product.repository.*
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.ImageVm
import com.ecommerce.app.product.viewmodel.product.*
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeGroupGetVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeValueVm
import io.micrometer.common.util.StringUtils
import lombok.extern.slf4j.Slf4j
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.ListUtils
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.Collectors


@Service
@Transactional
@Slf4j
class ProductService(
    private val productRepository: ProductRepository,
    private val mediaService: MediaService,
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val productImageRepository: ProductImageRepository,
    private val productOptionRepository: ProductOptionRepository,
    private val productOptionValueRepository: ProductOptionValueRepository,
    private val productOptionCombinationRepository: ProductOptionCombinationRepository,
    private val productRelatedRepository: ProductRelatedRepository
) {
    private val NONE_GROUP: String = "None group"


    fun createProduct(productPostVm: ProductPostVm): ProductGetDetailVm {
        validateProductVm(productPostVm)

        val mainProduct = buildMainProductFromVm(productPostVm)

        setProductBrand(productPostVm.brandId, mainProduct)
        var savedMainProduct = productRepository.save(mainProduct)

        val productCategories = setProductCategories(productPostVm.categoryIds, savedMainProduct)
        val productImages = setProductImages(productPostVm.productImageIds, savedMainProduct)
        productImageRepository.saveAll(productImages)
        productCategoryRepository.saveAll(productCategories)

        createProductRelations(productPostVm, savedMainProduct)
        if (productPostVm.variations.isEmpty() || productPostVm.productOptionValues.isEmpty()) {
            return ProductGetDetailVm.fromModel(savedMainProduct)
        }

        val savedVariations = createProductVariationsFromPostVm(productPostVm.variations, savedMainProduct)

        val optionsById = getProductOptionByIdMap(productPostVm.productOptionValues)
        val savedOptionValues = createProductOptionValues(productPostVm, savedMainProduct, optionsById)

        savedMainProduct.hasOptions = savedVariations.isNotEmpty() && savedOptionValues.isNotEmpty()
        savedMainProduct = productRepository.save(savedMainProduct)

        createOptionCombinations(productPostVm.variations, savedVariations, optionsById, savedOptionValues)

        return ProductGetDetailVm.fromModel(savedMainProduct)

    }

    private fun <T: ProductVariationSaveVm> validateProductVm(productSaveVm: ProductSaveVm<T>) {
        validateProductVm(productSaveVm, null)
    }

    private fun <T: ProductVariationSaveVm> validateProductVm(productSaveVm: ProductSaveVm<T>, existingProduct: Product?) {
        validateExistingProductProperties(productSaveVm, existingProduct)
        validateProductVariationDuplicates(productSaveVm)

        val variationIds = productSaveVm.variations().stream()
            .map(ProductProperties::id)
            .filter(Objects::nonNull).toList()

        val existingVariationsById = productRepository.findAllById(variationIds).stream()
            .collect(Collectors.toMap(Product::id, Function.identity()))

        for (variation in productSaveVm.variations()) {
            val existingVariation = existingVariationsById[variation.id()]
            validateExistingProductProperties(variation, existingVariation)
        }
    }

    private fun validateExistingProductProperties(productProperties: ProductProperties, existingProduct: Product?) {
        productProperties.slug()?.let {
            checkPropertyExists(
                it.lowercase(), existingProduct, productRepository::findBySlugAndIsPublishedTrue,
                Constants.ErrorCode.SLUG_ALREADY_EXISTED_OR_DUPLICATED)
        }
        if (StringUtils.isNotEmpty(productProperties.gtin())) {
            productProperties.gtin()?.let {
                checkPropertyExists(
                    it, existingProduct,
                    productRepository::findByGtinAndIsPublishedTrue,
                    Constants.ErrorCode.GTIN_ALREADY_EXISTED_OR_DUPLICATED)
            }
        }
        productProperties.sku()?.let {
            checkPropertyExists(
                it, existingProduct,
                productRepository::findBySkuAndIsPublishedTrue, Constants.ErrorCode.SKU_ALREADY_EXISTED_OR_DUPLICATED)
        }
    }

    private fun checkPropertyExists(propertyValue: String, existingProduct: Product?,finder: (String) -> Optional<Product>, errorCode: String) {
        finder(propertyValue).ifPresent { foundProduct ->
            if (existingProduct == null || foundProduct.id != existingProduct.id) {
                throw DuplicatedException(errorCode, propertyValue)
            }
        }
    }

    private fun <T: ProductVariationSaveVm> validateProductVariationDuplicates(productSaveVm: ProductSaveVm<T>) {
        val seenSlugs = mutableSetOf(productSaveVm.slug()?.lowercase())
        val seenSkus = mutableSetOf(productSaveVm.sku())
        val seenGtins = mutableSetOf(productSaveVm.gtin())

        for (variation in productSaveVm.variations()) {
            if (!variation.slug()?.let { seenSlugs.add(it.lowercase()) }!!) {
                throw DuplicatedException(Constants.ErrorCode.SLUG_ALREADY_EXISTED_OR_DUPLICATED)
            }

            if (variation.gtin()!!.isNotEmpty() && !seenGtins.add(variation.gtin())) {
                throw DuplicatedException(Constants.ErrorCode.GTIN_ALREADY_EXISTED_OR_DUPLICATED)
            }

            if (!seenSkus.add(variation.sku())) {
                throw DuplicatedException(Constants.ErrorCode.SKU_ALREADY_EXISTED_OR_DUPLICATED)
            }
        }
    }

    private fun buildMainProductFromVm(productPostVm: ProductPostVm): Product {
        return Product(
            name = productPostVm.name,
            thumbnailMediaId = productPostVm.thumbnailMediaId,
            slug = productPostVm.slug,
            description = productPostVm.description,
            shortDescription = productPostVm.shortDescription,
            specification = productPostVm.specification,
            sku = productPostVm.sku,
            gtin = productPostVm.gtin,
            weight = productPostVm.weight,
            dimensionUnit = productPostVm.dimensionUnit,
            length = productPostVm.length,
            width = productPostVm.width,
            height = productPostVm.height,
            price = productPostVm.price,
            isAllowedToOrder = productPostVm.isAllowedToOrder,
            isPublished = productPostVm.isPublished,
            isFeatured = productPostVm.isFeatured,
            isVisibleIndividually = productPostVm.isVisibleIndividually,
            stockTrackingEnabled = productPostVm.stockTrackingEnabled,
            metaTitle = productPostVm.metaTitle,
            metaKeyword = productPostVm.metaKeyword,
            metaDescription = productPostVm.description,
            hasOptions = CollectionUtils.isNotEmpty(productPostVm.variations)
                    && CollectionUtils.isNotEmpty(productPostVm.productOptionValues),
            productCategories = mutableListOf(),
            taxClassId = productPostVm.taxClassId)
    }

    private fun createProductVariationsFromPostVm(newVariationVms: List<ProductVariationPostVm>, mainProduct: Product): List<Product> {
        val allVariationImages = mutableListOf<ProductImage>()
        return performCreateVariations(newVariationVms, mainProduct, allVariationImages)
    }

    private fun createProductVariationsFromPutVm(newVariationVms: List<ProductVariationPutVm>, mainProduct: Product,
                                                 existingVariationImages: List<ProductImage>): List<Product> {
        val allVariationImages = existingVariationImages.toMutableList()
        return performCreateVariations(newVariationVms, mainProduct, allVariationImages)
    }

    private fun performCreateVariations(
        newVariationVms: List<out ProductVariationSaveVm>,
        mainProduct: Product,
        allVariationImages: MutableList<ProductImage>
    ): List<Product> {
        val productVariations = newVariationVms.map { variation ->
            val productVariation = buildProductVariationFromVm(variation, mainProduct)
            val variationImages = setProductImages(variation.productImageIds(), productVariation)
            allVariationImages.addAll(variationImages)
            productVariation
        }

        val savedVariations = productRepository.saveAll(productVariations)
        productImageRepository.saveAll(allVariationImages)
        return savedVariations
    }

    private fun buildProductVariationFromVm(variationVm: ProductVariationSaveVm, mainProduct: Product): Product {
        return Product(
            name = variationVm.name(),
            thumbnailMediaId = variationVm.thumbnailMediaId(),
            slug = variationVm.slug()?.lowercase(),
            sku = variationVm.sku(),
            gtin = variationVm.gtin(),
            price = variationVm.price(),
            isPublished = mainProduct.isPublished,
            parent = mainProduct
        )
    }

    private fun setProductImages(imageMediaIds: List<Long>?, product: Product): List<ProductImage> {
        val productImages = mutableListOf<ProductImage>()

        if (imageMediaIds?.isEmpty()!!) {
            product.id?.let { productImageRepository.deleteByProductId(it) }
            return productImages
        }

        if (product.productImages == null) {
            imageMediaIds.forEach { id ->
                productImages.add(ProductImage(imageId = id, product = product))
            }
        } else {
            val productImageIds = product.productImages.map { it.imageId }
            val newImageIds = imageMediaIds.filterNot { productImageIds.contains(it) }
            val deletedImageIds = productImageIds.filterNot { imageMediaIds.contains(it) }

            if (newImageIds.isNotEmpty()) {
                newImageIds.forEach { id ->
                    productImages.add(ProductImage(imageId = id, product = product))
                }
            }

            if (deletedImageIds.isNotEmpty()) {
                productImageRepository.deleteByImageIdInAndProductId(deletedImageIds, product.id)
            }
        }

        return productImages
    }

    private fun setProductBrand(brandId: Long?, product: Product) {
        if (brandId != null && (product.brand == null || brandId != product.brand?.id)) {
            val brand = brandRepository.findById(brandId).orElseThrow {
                NotFoundException(Constants.ErrorCode.BRAND_NOT_FOUND, brandId)
            }
            product.brand = brand
        }
    }

    private fun setProductCategories(vmCategoryIds: List<Long>, product: Product): List<ProductCategory> {
        val productCategoryList = mutableListOf<ProductCategory>()

        if (vmCategoryIds.isNotEmpty()) {
            val categoryIds = product.productCategories
                ?.mapNotNull { it.category?.id }
                ?.sorted()

            if (categoryIds != vmCategoryIds.sorted()) {
                val categoryList: List<Category> = categoryRepository.findAllById(vmCategoryIds)

                when {
                    categoryList.isEmpty() -> throw BadRequestException(Constants.ErrorCode.CATEGORY_NOT_FOUND, vmCategoryIds)
                    categoryList.size < vmCategoryIds.size -> {
                        val existingIds = categoryList.map { it.id }
                        val missingIds = vmCategoryIds.filterNot { existingIds.contains(it) }
                        throw BadRequestException(Constants.ErrorCode.CATEGORY_NOT_FOUND, missingIds)
                    }
                    else -> {
                        for (category in categoryList) {
                            productCategoryList.add(ProductCategory(product = product, category = category))
                        }
                    }
                }
            }
        }

        return productCategoryList
    }

    private fun createProductRelations(productPostVm: ProductPostVm, savedMainProduct: Product) {
        if (CollectionUtils.isEmpty(productPostVm.relatedProductIds)) {
            return
        }
        val relatedProducts = productPostVm.relatedProductIds.let { productRepository.findAllById(it) }
        val productRelations = relatedProducts.stream()
            .map { relatedProduct -> ProductRelated(
                product = savedMainProduct,
                relatedProduct = relatedProduct
            ) }
            .toList()
        productRelatedRepository.saveAll(productRelations)
    }

    private fun getProductOptionByIdMap(optionValueVms: List<out ProductOptionValueSaveVm>): Map<Long, ProductOption> {
        val productOptionIds = optionValueVms.map { it.productOptionId() }
        val productOptions = productOptionRepository.findAllByIdIn(productOptionIds)

        if (productOptions.isEmpty()) {
            throw BadRequestException(Constants.ErrorCode.NO_MATCHING_PRODUCT_OPTIONS)
        }

        return productOptions.associateBy { it.id!! }
    }

    private fun createProductOptionValues(
        productPostVm: ProductPostVm,
        savedMainProduct: Product,
        optionsById: Map<Long, ProductOption>
    ): List<ProductOptionValue> {
        val optionValues = mutableListOf<ProductOptionValue>()

        productPostVm.productOptionValues.forEach { optionValueVm ->
            optionValueVm.value.forEach { value ->
                val optionValue =
                    ProductOptionValue(
                    product = savedMainProduct,
                    displayOrder = optionValueVm.displayOrder,
                    displayType = optionValueVm.displayType,
                    productOption = optionsById[optionValueVm.productOptionId],
                    value = value
                )

                optionValues.add(optionValue)
            }
        }

        return productOptionValueRepository.saveAll(optionValues)
    }

    private fun createOptionCombinations(
        variationVms: List<out ProductVariationSaveVm>,
        savedVariations: List<Product>,
        optionsById: Map<Long, ProductOption>,
        optionValues: List<ProductOptionValue>
    ) {
        val optionCombinations = mutableListOf<ProductOptionCombination>()
        val variationBySlug = savedVariations.associateBy { it.slug }

        for (variationVm in variationVms) {
            val variationSlug = variationVm.slug()?.lowercase()
            val savedVariation = variationBySlug[variationSlug]
                ?: run {
                    log.error(
                        "Failed to create combinations: variation with slug '$variationSlug' not found in the saved variations map"
                    )
                    throw InternalServerErrorException(Constants.ErrorCode.PRODUCT_COMBINATION_PROCESSING_FAILED)
                }
            variationVm.optionValuesByOptionId().forEach { (optionId, optionValue) ->
                val productOption = optionsById[optionId]
                val foundOptionValue = optionValues.find {
                    it.productOption?.id == optionId && it.value == optionValue
                } ?: throw BadRequestException(Constants.ErrorCode.PRODUCT_OPTION_VALUE_IS_NOT_FOUND,
                    optionValue)
                val optionCombination = ProductOptionCombination(
                    product = savedVariation,
                    productOption = productOption,
                    value = foundOptionValue.value,
                    displayOrder = foundOptionValue.displayOrder
                )
                optionCombinations.add(optionCombination)
            }
        }
        productOptionCombinationRepository.saveAll(optionCombinations)
    }

    fun getProductsWithFilter(pageNo: Int, pageSize: Int, productName: String?, brandName: String?): ProductListGetVm {
        val pageable: Pageable = PageRequest.of(pageNo, pageSize)
        val productPage: Page<Product> = productRepository.getProductsWithFilter(
            productName?.trim()?.lowercase() ?: "",
            brandName?.trim() ?: "",
            pageable
        )

        val productList: List<Product> = productPage.content
        val productListVmList: List<ProductListVm> = productList.map { ProductListVm.fromModel(it) }

        return ProductListGetVm(
            productListVmList,
            productPage.number,
            productPage.size,
            productPage.totalElements.toInt(),
            productPage.totalPages,
            productPage.isLast
        )
    }

    fun getProductById(productId: Long): ProductDetailVm {
        val product = productRepository
            .findById(productId)
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, productId) }
//        val productImageMedias = mutableListOf<ImageVm?>()
//        product.productImages?.forEach { image ->
//           productImageMedias.add(
//               mediaService.getMedia(image.imageId)?.let { ImageVm(image.imageId, it.url) }
//           )
//        }
//        val thumbnailMedia = product.thumbnailMediaId?.let {
//            mediaService.getMedia(it)?.let { it1 -> ImageVm(it, it1.url) }
//        }
        val categories = mutableListOf<Category?>()
        product.productCategories?.forEach { category ->
            categories.add(category.category)
        }

        val brandId = product.brand?.id

        return ProductDetailVm(
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
            product.dimensionUnit,
            product.weight,
            product.length,
            product.width,
            product.height,
            brandId,
            categories,
            product.metaTitle,
            product.metaKeyword,
            product.metaDescription,
            thumbnailMedia = null,
            productImageMedias = mutableListOf(),
            product.taxClassId
        )
    }

    fun exportProducts(productName: String, brandName: String): List<ProductExportingDetailVm> {
        val productList = productRepository.getExportingProducts(productName.trim().lowercase(), brandName.trim())

        return productList.stream()
            .map { product -> ProductExportingDetailVm(
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
                product.brand?.id,
                product.brand?.name,
                product.metaTitle,
                product.metaKeyword,
                product.metaDescription
            ) }
            .toList()
    }

    fun updateProduct(productId: Long, productPutVm: ProductPutVm) {
        val product = productRepository.findById(productId).orElseThrow {
            NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, productId)
        }

        validateProductVm(productPutVm, product)
        setProductBrand(productPutVm.brandId, product)
        updateMainProductFromVm(productPutVm, product)

        val productImages = setProductImages(productPutVm.productImageIds, product)
        productImageRepository.saveAll(productImages)
        val allVariationImages = mutableListOf<ProductImage>()
        val existingVariations: List<Product> = product.products!!
        updateExistingVariants(productPutVm, allVariationImages, existingVariations)
        productRepository.saveAll(existingVariations)

        val newVariationVms = productPutVm.variations.stream()
            .filter { _ -> false }.toList()
        if (CollectionUtils.isEmpty(newVariationVms)) return

        val newSavedVariations = createProductVariationsFromPutVm(newVariationVms, product, allVariationImages)
        val optionsById = getProductOptionByIdMap(productPutVm.productOptionValues)
        val productOptionValues = updateProductOptionValues(productPutVm, product, optionsById)
        product.hasOptions = CollectionUtils.isNotEmpty(newSavedVariations) || CollectionUtils.isNotEmpty(existingVariations)
                && CollectionUtils.isNotEmpty(productOptionValues)
        createOptionCombinations(newVariationVms, newSavedVariations, optionsById, productOptionValues)
    }

    fun updateMainProductFromVm(productPutVm: ProductPutVm, product: Product) {
        product.name = productPutVm.name
        product.slug = productPutVm.slug
        product.thumbnailMediaId = productPutVm.thumbnailMediaId
        product.description = productPutVm.description
        product.shortDescription = productPutVm.shortDescription
        product.specification = productPutVm.specification
        product.sku = productPutVm.sku
        product.gtin = productPutVm.gtin
        product.price = productPutVm.price
        product.isAllowedToOrder = productPutVm.isAllowedToOrder
        product.isFeatured = productPutVm.isFeatured
        product.isPublished = productPutVm.isPublished
        product.isVisibleIndividually = productPutVm.isVisibleIndividually
        product.stockTrackingEnabled = productPutVm.stockTrackingEnabled
        product.metaTitle = productPutVm.metaTitle
        product.metaKeyword = productPutVm.metaKeyword
        product.metaDescription = productPutVm.metaDescription
        product.taxClassId = productPutVm.taxClassId
        product.weight = productPutVm.weight
        product.dimensionUnit = productPutVm.dimensionUnit
        product.width = productPutVm.width
        product.length = productPutVm.length
        product.height = productPutVm.height

    }

    private fun updateProductOptionValues(
        productPutVm: ProductPutVm,
        product: Product,
        optionsById: Map<Long, ProductOption>
    ): List<ProductOptionValue> {
        productOptionValueRepository.deleteAllByProductId(product.id)
        val productOptionValues = mutableListOf<ProductOptionValue>()
        productPutVm.productOptionValues.forEach { optionValueVm -> optionValueVm.value.forEach { value ->
            val optionValue = ProductOptionValue(
                product = product,
                displayOrder =  optionValueVm.displayOrder,
                displayType = optionValueVm.displayType,
                productOption = optionsById[optionValueVm.productOptionId],
                value = value
            )
            productOptionValues.add(optionValue)
        } }
        productOptionValueRepository.saveAll(productOptionValues)
        return productOptionValues
    }

    private fun updateExistingVariants(
        productPutVm: ProductPutVm,
        newProductImages: MutableList<ProductImage>,
        existingVariants: List<Product>
    ) {
        productPutVm.variations()?.let { variations ->
            variations.forEach { variant ->
                variant.id()?.let { variantId ->
                    val variantInDb = existingVariants.firstOrNull { it.id == variantId }
                    if (variantInDb != null) {
                        setValuesForVariantExisting(newProductImages, variant, variantInDb)
                    }
                }
            }
        }
    }


    private fun setValuesForVariantExisting(
        newProductImages: MutableList<ProductImage>,
        variant: ProductVariationPutVm,
        variantInDb: Product
    ) {
        variantInDb.let {
            it.name = variant.name()
            it.thumbnailMediaId = variant.thumbnailMediaId()
            it.slug = variant.slug()?.lowercase()
            it.sku = variant.sku()
            it.gtin = variant.gtin()
            it.price = variant.price()

            productImageRepository.deleteByProductId(variant.id)

            variant.productImageIds().let { imageIds ->
                if (imageIds.isNotEmpty()) {
                    imageIds.forEach { imageId ->
                        newProductImages.add(
                            ProductImage(imageId = imageId, product = variantInDb)
                        )
                    }
                }
            }

            productImageRepository.saveAll(newProductImages)
        }
    }

    fun getListFeaturedProducts(pageNo: Int, pageSize: Int): ProductFeatureGetVm {
        val pageable: Pageable = PageRequest.of(pageNo, pageSize)
        val productThumbnailVms = mutableListOf<ProductThumbnailGetVm>()
        val productPage = productRepository.getFeaturedProduct(pageable)
        val products = productPage.content
        for (product in products) {
            productThumbnailVms.add(
                ProductThumbnailGetVm(
                product.id!!,
                product.name!!,
                product.slug!!,
//                mediaService.getMedia(product.thumbnailMediaId)!!.url,
                    null,
                product.price!!
            )
            )
        }
        return ProductFeatureGetVm(productThumbnailVms, productPage.totalPages)
    }

    fun getProductsByBrand(brandSlug: String): List<ProductThumbnailVm> {
        val productThumbnailVms = mutableListOf<ProductThumbnailVm>()
        val brand = brandRepository
            .findBySlug(brandSlug)
            .orElseThrow { NotFoundException(Constants.ErrorCode.BRAND_NOT_FOUND, brandSlug) }
        val products = productRepository.findAllByBrandAndIsPublishedTrueOrderByIdAsc(brand)
        for (product in products) {
            productThumbnailVms.add(
                ProductThumbnailVm(
                product.id!!,
                product.name!!,
                product.slug!!,
//                mediaService.getMedia(product.thumbnailMediaId)!!.url
                null
            )
            )
        }
        return productThumbnailVms
    }

    fun getProductsFromCategory(pageNo: Int, pageSize: Int, categorySlug: String): ProductListGetFromCategoryVm {
        val productThumbnailVms = mutableListOf<ProductThumbnailVm>()
        val pageable = PageRequest.of(pageNo, pageSize)
        val category = categoryRepository
            .findBySlug(categorySlug)
            .orElseThrow { NotFoundException(Constants.ErrorCode.CATEGORY_NOT_FOUND, categorySlug) }
        val productCategoryPage = productCategoryRepository.findAllByCategory(pageable, category)
        val productList = productCategoryPage.content
        val products = productList.stream()
            .map { it.product }
            .toList()
        for (product in products) {
            if (product != null) {
                productThumbnailVms.add(
                    ProductThumbnailVm(
                        product.id!!,
                        product.name!!,
                        product.slug!!,
                        null,
        //                mediaService.getMedia(product.thumbnailMediaId).!url
                    )
                )
            }
        }
        return ProductListGetFromCategoryVm(
            productThumbnailVms,
            productCategoryPage.number,
            productCategoryPage.size,
            productCategoryPage.totalElements.toInt(),
            productCategoryPage.totalPages,
            productCategoryPage.isLast
        )
    }

    fun getFeaturedProductsById(productIds: List<Long>): List<ProductThumbnailGetVm> {
        val products = productRepository.findAllByIdIn(productIds)
        return products.map { product ->

            val thumbnailUrl = mediaService.getMedia(product.thumbnailMediaId)?.url
            if (thumbnailUrl!!.isNotEmpty() || product.parent == null) {
                ProductThumbnailGetVm(
                    product.id!!,
                    product.name!!,
                    product.slug!!,
//                    thumbnailUrl,
                    null,
                    product.price!!
                )
            } else {
//                val parentProduct = productRepository.findById(product.parent.id!!)

                ProductThumbnailGetVm(
                    product.id!!,
                    product.name!!,
                    product.slug!!,
//                    parentProduct.map { pr -> mediaService.getMedia(pr.thumbnailMediaId).url() }
//                        .orElse(""),
                    null,
                    product.price!!
                )
            }
        }
    }

    fun getProductDetail(slug: String): ProductDetailGetVm {
        val product = productRepository.findBySlugAndIsPublishedTrue(slug)
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, slug) }

        val productThumbnailMediaId = product.thumbnailMediaId
        val productThumbnailUrl = mediaService.getMedia(productThumbnailMediaId)?.url ?: ""

        val productImageMediaUrls = if (product.productImages!!.isNotEmpty()) {
            product.productImages.map { image ->
                mediaService.getMedia(image.imageId)?.url ?: ""
            }
        } else {
            emptyList()
        }

        val productAttributeGroupsVm = mutableListOf<ProductAttributeGroupGetVm>()
        val productAttributeValues = product.attributeValues

        if (productAttributeValues != null) {
            if (productAttributeValues.isNotEmpty()) {
                val productAttributeGroups = productAttributeValues
                    .map { it.productAttribute?.productAttributeGroup }
                    .distinct()

                productAttributeGroups.forEach { productAttributeGroup ->
                    val productAttributeValueVms = productAttributeValues
                        .filter { it.productAttribute?.productAttributeGroup == productAttributeGroup }
                        .map { productAttributeValue ->
                            ProductAttributeValueVm(
                                productAttributeValue.productAttribute?.name,
                                productAttributeValue.value
                            )
                        }

                    val productAttributeGroupName = productAttributeGroup?.name ?: NONE_GROUP
                    val productAttributeGroupVm = ProductAttributeGroupGetVm(
                        productAttributeGroupName,
                        productAttributeValueVms
                    )
                    productAttributeGroupsVm.add(productAttributeGroupVm)
                }
            }
        }

        return ProductDetailGetVm(
            product.id,
            product.name,
            product.brand?.name,
            product.productCategories?.map { it.category?.name }!!,
            productAttributeGroupsVm,
            product.shortDescription,
            product.description,
            product.specification,
            product.isAllowedToOrder,
            product.isPublished,
            product.isFeatured,
            product.hasOptions,
            product.price,
//            productThumbnailUrl,
//            productImageMediaUrls
            null,
            null
        )
    }

    fun deleteProduct(id: Long) {
        val product = productRepository.findById(id).orElseThrow {
            NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, id)
        }
        product.isPublished = false
        if (!Objects.isNull(product.parent)) {
            val productOptionCombinationList = productOptionCombinationRepository
                .findAllByProduct(product)
            if (CollectionUtils.isNotEmpty(productOptionCombinationList)) {
                productOptionCombinationRepository.deleteAll(productOptionCombinationList)
            }
        }
        productRepository.save(product)
    }

    fun getProductsByMultiQuery(
        pageNo: Int,
        pageSize: Int,
        productName: String,
        categorySlug: String,
        startPrice: Double?,
        endPrice: Double?
    ): ProductsGetVm {
        val pageable: Pageable = PageRequest.of(pageNo, pageSize)
        val productPage: Page<Product> = productRepository.findByProductNameAndCategorySlugAndPriceBetween(
            productName.trim().lowercase(),
            categorySlug.trim(), startPrice, endPrice, pageable
        )
        val productThumbnailVms = mutableListOf<ProductThumbnailGetVm>()
        val products = productPage.content
        for (product in products) {
            productThumbnailVms.add(
                ProductThumbnailGetVm(
                product.id,
                product.name,
                product.slug,
//                mediaService.getMedia(product.thumbnailMediaId)?.url,
                null,
                product.price
            )
            )
        }
        return ProductsGetVm(
            productThumbnailVms,
            productPage.number,
            productPage.size,
            productPage.totalElements.toInt(),
            productPage.totalPages,
            productPage.isLast
        )
    }

    fun getProductVariationsByParentId(id: Long): List<ProductVariationGetVm> {
        val parentProduct = productRepository.findById(id)
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, id) }

        if (parentProduct.hasOptions == true) {
            val productVariations = parentProduct.products?.filter { it.isPublished == true }

            return productVariations!!.map { product ->
                val productOptionCombinations = productOptionCombinationRepository.findAllByProduct(product)
                val options = productOptionCombinations.associate {
                    it.productOption?.id to it.value
                }

                val image = product.thumbnailMediaId?.let {
                    ImageVm(it, mediaService.getMedia(it)?.url!!)
                }

                ProductVariationGetVm(
                    product.id,
                    product.name,
                    product.slug,
                    product.sku,
                    product.gtin,
                    product.price,
                    image,
//                    product.productImages?.map { productImage ->
//                        ImageVm(productImage.imageId, mediaService.getMedia(productImage.imageId)?.url!!)
//                    },
                    null,
                    options
                )
            }
        }
        return emptyList()
    }

    fun getProductSlug(id: Long): ProductSlugGetVm {
        val product = productRepository.findById(id)
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, id) }
        val parent = product.parent
        if (parent != null) {
            return ProductSlugGetVm(parent.slug, id)
        }
        return ProductSlugGetVm(product.slug, null)
    }

    fun getProductEsDetailById(productId: Long): ProductEsDetailVm {
        val product = productRepository
            .findById(productId)
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, productId) }
        var thumbnailMediaId: Long? = null
        if (null != product.thumbnailMediaId) {
            thumbnailMediaId = product.thumbnailMediaId
        }
        val categoryNames: List<String>? = product.productCategories
            ?.mapNotNull { it.category?.name }

        val attributeNames: List<String>? = product.attributeValues
            ?.mapNotNull { it.productAttribute?.name }

        var brandName: String? = null
        if (null != product.brand) {
            brandName = product.brand!!.name
        }
        return ProductEsDetailVm(
            product.id,
            product.name,
            product.slug,
            product.price,
            product.isPublished,
            product.isVisibleIndividually,
            product.isAllowedToOrder,
            product.isFeatured,
            thumbnailMediaId,
            brandName,
            categoryNames,
            attributeNames
        )
    }

    fun getRelatedProductsBackoffice(id: Long): List<ProductListVm> {
        val product = productRepository.findById(id)
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, id) }
        val relatedProducts = product.relatedProducts
        return relatedProducts?.stream()
            ?.map { productRelated ->
                ProductListVm(
                    productRelated.relatedProduct?.id,
                    productRelated.relatedProduct?.name,
                    productRelated.relatedProduct?.slug,
                    productRelated.relatedProduct?.isAllowedToOrder,
                    productRelated.relatedProduct?.isPublished,
                    productRelated.relatedProduct?.isFeatured,
                    productRelated.relatedProduct?.isVisibleIndividually,
                    productRelated.relatedProduct?.price,
                    productRelated.relatedProduct?.createdOn,
                    productRelated.relatedProduct?.taxClassId,
                    productRelated.relatedProduct?.parent?.id
                )
            }!!.toList()
    }

    fun getRelateProductsStorefront(id: Long, pageNo: Int, pageSize: Int): ProductsGetVm {
        val product = productRepository.findById(id)
            .orElseThrow { NotFoundException(Constants.ErrorCode.PRODUCT_NOT_FOUND, id) }
        val relatedProductsPage = productRelatedRepository.findAllByProduct(product, PageRequest.of(pageNo, pageSize))
        val productThumbnailVms: List<ProductThumbnailGetVm> = relatedProductsPage
            .filter { it.relatedProduct?.isPublished == true }
            .map { productRelated ->
                val relatedProduct = productRelated.relatedProduct
                ProductThumbnailGetVm(
                    relatedProduct?.id,
                    relatedProduct?.name,
                    relatedProduct?.slug,
//                    mediaService.getMedia(relatedProduct.thumbnailMediaId).url,
                    null,
                    relatedProduct?.price
                )
            }
            .toList()
        return ProductsGetVm(
//            productThumbnailVms,
            null,
            relatedProductsPage.number,
            relatedProductsPage.size,
            relatedProductsPage.totalElements.toInt(),
            relatedProductsPage.totalPages,
            relatedProductsPage.isLast
        )
    }


    fun getProductsForWarehouse(
        name: String, sku: String, productIds: List<Long>?, selection: FilterExistInWhenSelection?
    ): List<ProductInfoVm> {
        return productRepository.findProductForWarehouse(name, sku, productIds, selection!!.name)
            .stream().map(ProductInfoVm::fromProduct).toList()
    }

    fun updateProductQuantity(productQuantityPostVms: List<ProductQuantityPostVm>) {
        val productIds = productQuantityPostVms.map { it.productId }
        val products = productRepository.findAllByIdIn(productIds)

        products.parallelStream().forEach { product ->
            val productQuantityPostVmOptional = productQuantityPostVms.parallelStream()
                .filter { it.productId == product.id }
                .findFirst()
            productQuantityPostVmOptional.ifPresent { productQuantityPostVm ->
                product.stockQuantity = productQuantityPostVm.stockQuantity
            }
        }

        productRepository.saveAll(products)
    }

    fun partitionUpdateStockQuantityByCalculation(
        productQuantityItems: List<ProductQuantityPutVm>,
        calculation: BiFunction<Long, Long, Long>
    ): Unit {
        val productIds = productQuantityItems.stream()
            .map(ProductQuantityPutVm::productId)
            .toList()
        val productQuantityItemMap = productQuantityItems.fold(mutableMapOf<Long, ProductQuantityPutVm>()) { acc, item ->
            acc.merge(item.productId, item, this::mergeProductQuantityItem)
            acc
        }

        val products = this.productRepository.findByCategoryIdsIn(productIds)
        products.forEach { product ->
            if (product.stockTrackingEnabled == true) {
                val amount = getRemainAmountOfStockQuantity(productQuantityItemMap, product, calculation)
                product.stockQuantity = amount
            }
        }
        this.productRepository.saveAll(products)
    }

    fun subtractStockQuantity(productQuantityItems: List<ProductQuantityPutVm>) {
        productQuantityItems.chunked(5).forEach {
            partitionUpdateStockQuantityByCalculation(it) { stock, quantity ->
                stock - quantity
            }
        }
    }



    private fun mergeProductQuantityItem(p1: ProductQuantityPutVm, p2: ProductQuantityPutVm): ProductQuantityPutVm {
        val q1 = p1.quantity
        val q2 = p2.quantity
        return ProductQuantityPutVm(p1.productId, q1 + q2)
    }


    private fun getRemainAmountOfStockQuantity(productQuantityItemMap: Map<Long, ProductQuantityPutVm>,
                                               product: Product, calculation: BiFunction<Long, Long, Long>): Long {
        val stockQuantity = product.stockQuantity
        val productItem = productQuantityItemMap[product.id]
        val quantity = productItem?.quantity
        return calculation.apply(stockQuantity!!, quantity!!)
    }

    fun getProductByIds(productIds: List<Long>): List<ProductListVm> {
        return productRepository.findAllByIdIn(productIds).map { ProductListVm.fromModel(it) }
    }

    fun getProductByCategoryIds(categoryIds: List<Long>): List<ProductListVm> {
        return productRepository.findByCategoryIdsIn(categoryIds).map { ProductListVm.fromModel(it) }
    }

    fun getProductByBrandIds(brandIds: List<Long>): List<ProductListVm> {
        return productRepository.findByBrandIdsIn(brandIds).map { ProductListVm.fromModel(it) }
    }

    fun getLatestProduct(count: Int): List<ProductListVm> {
        if (count <= 0) {
            return mutableListOf()
        }
        val pageable = PageRequest.of(0, count)
        val products = productRepository.getLatestProducts(pageable)

        if (CollectionUtils.isEmpty(products)) {
            return mutableListOf()
        }
        return products.stream()
            .map { ProductListVm.fromModel(it) }
            .toList()
    }

    companion object {
        private val log = LoggerFactory.getLogger(ProductService::class.java)
    }
}
