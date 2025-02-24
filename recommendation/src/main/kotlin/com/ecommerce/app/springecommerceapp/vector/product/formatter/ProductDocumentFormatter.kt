package com.ecommerce.app.springecommerceapp.vector.product.formatter

import com.ecommerce.app.springecommerceapp.vector.common.formatter.DocumentFormatter
import com.ecommerce.app.springecommerceapp.viewmodel.CategoryVm
import com.ecommerce.app.springecommerceapp.viewmodel.ProductAttributeValueVm
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.text.StringSubstitutor

class ProductDocumentFormatter : DocumentFormatter {

    override fun format(entityMap: MutableMap<String, Any>, template: String, objectMapper: ObjectMapper): String {
        entityMap.compute("attributeValues") { _, attributeValues -> formatAttributes(attributeValues, objectMapper) }
        entityMap.compute("categories") { _, categoriesValues -> formatCategories(categoriesValues, objectMapper) }

        val sub = StringSubstitutor(entityMap, "{", "}")
        return removeHtmlTags(sub.replace(template))
    }

    private fun formatAttributes(attributeValues: Any?, objectMapper: ObjectMapper): String {
        if (attributeValues == null) {
            return "[]"
        }
        val attributeValuesNew = attributeValues as List<*>
        val productAttributeValueList = attributeValuesNew.map {
            objectMapper.convertValue(it, ProductAttributeValueVm::class.java)
        }

        return productAttributeValueList.joinToString(", ", "[", "]") {
            "${it.nameProductAttribute}: ${it.value}"
        }
    }

    private fun formatCategories(categories: Any?, objectMapper: ObjectMapper): String {
        if (categories == null) {
            return "[]"
        }
        val categoriesNew = categories as List<*>
        val categoriesList = categoriesNew.map {
            objectMapper.convertValue(it, CategoryVm::class.java)
        }

        return categoriesList.joinToString(", ", "[", "]") { it.name }
    }
}
