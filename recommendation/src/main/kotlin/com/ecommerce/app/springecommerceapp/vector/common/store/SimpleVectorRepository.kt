package com.ecommerce.app.springecommerceapp.vector.common.store

import com.ecommerce.app.springecommerceapp.configuration.EmbeddingSearchConfiguration
import com.ecommerce.app.springecommerceapp.vector.common.document.BaseDocument
import com.ecommerce.app.springecommerceapp.vector.common.document.DefaultIdGenerator
import com.ecommerce.app.springecommerceapp.vector.common.document.DocumentMetadata
import com.ecommerce.app.springecommerceapp.vector.common.formatter.DocumentFormatter
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.SneakyThrows
import org.springframework.ai.document.Document
import org.springframework.ai.document.id.IdGenerator
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.filter.Filter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class SimpleVectorRepository<D: BaseDocument, E>: VectorRepository<D, E> {

    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @Autowired
    private lateinit var embeddingSearchConfiguration: EmbeddingSearchConfiguration

    lateinit var  docType: Class<D>
    lateinit var vectorStore: VectorStore
    lateinit var documentMetadata: DocumentMetadata
    lateinit var documentFormatter: DocumentFormatter

    companion object {
        const val FIELD_ID = "id"
        const val TYPE_METADATA = "type"
    }

    @SneakyThrows
    override fun add(entityId: Long) {
        val entity = getEntity(entityId)
        val entityContentMap = (objectMapper.convertValue(entity, Map::class.java) as Map<String, Any>).toMutableMap()

        val document = docType.getDeclaredConstructor().newInstance()
        document.content = documentFormatter.format(entityContentMap, documentMetadata.contentFormat, objectMapper)

        entityContentMap[TYPE_METADATA] = documentMetadata.docIdPrefix
        document.metadata = entityContentMap

        val idGenerator = getIdGenerator(entityId)
        vectorStore.add(listOf(document.toDocument(idGenerator)))
    }

    override fun delete(entityId: Long) {
        val idGenerator = getIdGenerator(entityId)
        vectorStore.delete(listOf(idGenerator.generateId(entityId)))
    }

    override fun update(entityId: Long) {
        delete(entityId)
        add(entityId)
    }

    override fun search(id: Long): List<D> {
        val entity = getEntity(id)
        val entityContentMap = objectMapper.convertValue(entity, Map::class.java) as MutableMap<String, Any>
        val content = documentFormatter.format(entityContentMap, documentMetadata.contentFormat, objectMapper)

        return vectorStore.similaritySearch(
            SearchRequest
                .query(content)
                .withTopK(embeddingSearchConfiguration.topK)
                .withFilterExpression(excludeSameEntityExpression(id))
                .withSimilarityThreshold(embeddingSearchConfiguration.similarityThreshold)
        ).map { toBaseDocument(it) }
    }

    fun getIdGenerator(entityId: Long): IdGenerator {
        return DefaultIdGenerator(entityId, documentMetadata.docIdPrefix)
    }

    private fun excludeSameEntityExpression(id: Long): Filter.Expression {
        val b = FilterExpressionBuilder()
        return b.ne(FIELD_ID, id).build()
    }

    @Throws(Exception::class)
    protected fun toBaseDocument(document: Document): D {
        val baseDocument = docType.getDeclaredConstructor().newInstance()
        baseDocument.content = document.content
        baseDocument.metadata = document.metadata
        return baseDocument
    }

    @Autowired
    fun setObjectMapper(objectMapper: ObjectMapper) {
        this.objectMapper = objectMapper
    }

    @Autowired
    fun setEmbeddingSearchConfiguration(embeddingSearchConfiguration: EmbeddingSearchConfiguration) {
        this.embeddingSearchConfiguration = embeddingSearchConfiguration
    }
}
