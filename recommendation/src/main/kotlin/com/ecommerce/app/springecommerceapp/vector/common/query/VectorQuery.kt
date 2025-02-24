package com.ecommerce.app.springecommerceapp.vector.common.query

import com.ecommerce.app.springecommerceapp.vector.common.document.BaseDocument
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.document.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class VectorQuery<D: BaseDocument, P>(
    val docType: Class<D>? = null,
    val resultType: Class<P>? = null
)  {
    private lateinit var objectMapper: ObjectMapper
    private lateinit var jdbcVectorService: JdbcVectorService

    fun similaritySearch(id: Long): List<P> {
        return toResult(jdbcVectorService.similarityProduct(id, docType!!))
    }

    protected fun toResult(documents: List<Document>): List<P> {
        return documents
            .filter { it.metadata != null }
            .map { objectMapper.convertValue(it.metadata, resultType) }
    }

    @Autowired
    fun setObjectMapper(objectMapper: ObjectMapper) {
        this.objectMapper = objectMapper
    }

    @Autowired
    fun setJdbcVectorService(jdbcVectorService: JdbcVectorService) {
        this.jdbcVectorService = jdbcVectorService
    }
}
