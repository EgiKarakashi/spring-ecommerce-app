package com.ecommerce.app.product.service

import com.ecommerce.app.commonlibrary.exception.BadRequestException
import com.ecommerce.app.commonlibrary.exception.DuplicatedException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.product.model.Category
import com.ecommerce.app.product.repository.CategoryRepository
import com.ecommerce.app.product.utils.Constants
import com.ecommerce.app.product.viewmodel.ImageVm
import com.ecommerce.app.product.viewmodel.category.CategoryGetDetailVm
import com.ecommerce.app.product.viewmodel.category.CategoryGetVm
import com.ecommerce.app.product.viewmodel.category.CategoryListGetVm
import com.ecommerce.app.product.viewmodel.category.CategoryPostVm
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val mediaService: MediaService
) {

    fun getPageableCategories(pageNo: Int, pageSize: Int): CategoryListGetVm {
        val categoryGetVms = mutableListOf<CategoryGetVm>()
        val pageable = PageRequest.of(pageNo, pageSize)
        val categoryPage = categoryRepository.findAll(pageable)
        val categories = categoryPage.content
        for (category in categories) {
            categoryGetVms.add(CategoryGetVm.fromModel(category))
        }

        return CategoryListGetVm(
            categoryGetVms,
            categoryPage.number,
            categoryPage.size,
            categoryPage.totalElements.toInt(),
            categoryPage.totalPages,
            categoryPage.isLast
        )
    }

    fun getCategories(categoryName: String): List<CategoryGetVm> {
        val category = categoryRepository.findByNameContainingIgnoreCase(categoryName)
        val categoryGetVms = mutableListOf<CategoryGetVm>()
        category.forEach { category ->
            val categoryImage: ImageVm? = if (category.imageId != null) {
                mediaService.getMedia(category.imageId)?.url?.let { ImageVm(category.imageId, it) }
            } else {
                null
            }

            val parent: Category? = category.parent
            val parentId = parent?.id ?: -1L

            val categoryGetVm = CategoryGetVm(
                category.id,
                category.name,
                category.slug,
                parentId,
                categoryImage
            )
            categoryGetVms.add(categoryGetVm)
        }
        return categoryGetVms
    }

    fun getCategoryById(id: Long): CategoryGetDetailVm {
        val category = categoryRepository.findById(id).orElseThrow { NotFoundException(Constants.ErrorCode.CATEGORY_NOT_FOUND, id) }
        val categoryImage: ImageVm? = if (category.imageId != null) {
            mediaService.getMedia(category.imageId)?.url?.let { ImageVm(category.imageId, it) }
        } else {
            null
        }
        val parentId = category.parent?.id ?: 0L
        return CategoryGetDetailVm(
            category.id,
            category.name,
            category.slug,
            category.description,
            parentId,
            category.metaKeyword,
            category.metaDescription,
            category.displayOrder,
            category.isPublished,
            categoryImage
        )
    }

    fun create(categoryPostVm: CategoryPostVm): Category {
        validateDuplicateName(categoryPostVm.name, null)
        val category = Category()
        category.name = categoryPostVm.name
        category.slug = categoryPostVm.slug
        category.description = categoryPostVm.description
        category.displayOrder = categoryPostVm.displayOrder
        category.metaDescription = categoryPostVm.metaDescription
        category.metaKeyword = categoryPostVm.metaKeywords
        category.isPublished = categoryPostVm.isPublish
        category.imageId = categoryPostVm.imageId
        if (categoryPostVm.parentId != null) {
            val parentCategory = categoryRepository
                .findById(categoryPostVm.parentId)
                .orElseThrow { BadRequestException(Constants.ErrorCode.PARENT_CATEGORY_NOT_FOUND, categoryPostVm.parentId) }
            category.parent = parentCategory
        }
        return categoryRepository.save(category)
    }

    fun update(categoryPostVm: CategoryPostVm, id: Long) {
        validateDuplicateName(categoryPostVm.name, id)
        val category = categoryRepository
            .findById(id)
            .orElseThrow { NotFoundException(Constants.ErrorCode.CATEGORY_NOT_FOUND, id) }
        category.name = categoryPostVm.name
        category.slug = categoryPostVm.slug
        category.description = categoryPostVm.description
        category.displayOrder = categoryPostVm.displayOrder
        category.metaDescription = categoryPostVm.metaDescription
        category.metaKeyword = categoryPostVm.metaKeywords
        category.isPublished = categoryPostVm.isPublish
        category.imageId = categoryPostVm.imageId
        if (categoryPostVm.parentId == null) {
            category.parent = null
        } else {
            val parentCategory = categoryRepository
                .findById(categoryPostVm.parentId)
                .orElseThrow { BadRequestException(Constants.ErrorCode.PARENT_CATEGORY_NOT_FOUND, categoryPostVm.parentId) }

            if (!checkParent(category.id, parentCategory)) {
                throw BadRequestException(Constants.ErrorCode.PARENT_CATEGORY_CANNOT_BE_ITSELF)
            }
            category.parent = parentCategory
        }
    }

    private fun checkExistedName(name: String, id: Long?): Boolean {
        return id?.let { categoryRepository.findExistedName(name, it) } != null
    }


    private fun validateDuplicateName(name: String, id: Long?) {
        if (checkExistedName(name, id)) {
            throw DuplicatedException(Constants.ErrorCode.NAME_ALREADY_EXITED, name)
        }
    }

    private fun checkParent(id: Long?, category: Category): Boolean {
        if (id == category.id) {
            return false
        }
        if (category.parent != null) {
            return checkParent(id, category.parent!!)
        } else {
            return true
        }
    }

    fun getCategoryByIds(ids: List<Long>): List<CategoryGetVm> {
        return categoryRepository.findAllById(ids).stream().map { CategoryGetVm.fromModel(it) }.toList()
    }
}
