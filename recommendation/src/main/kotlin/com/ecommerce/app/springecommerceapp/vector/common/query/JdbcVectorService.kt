package com.ecommerce.app.springecommerceapp.vector.common.query

import com.ecommerce.app.springecommerceapp.configuration.EmbeddingSearchConfiguration
import com.ecommerce.app.springecommerceapp.vector.common.document.BaseDocument
import com.ecommerce.app.springecommerceapp.vector.common.document.DocumentMetadata
import org.springframework.ai.document.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.StatementCreatorUtils
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class JdbcVectorService(
    @Value("\${spring.ai.vectorstore.pgvector.table-name:vector_store}")
    val vectorTableName: String,
    val jdbcClient: JdbcTemplate,
    val documentRowMapper: DocumentRowMapper,
    val embeddingSearchConfiguration: EmbeddingSearchConfiguration
) {
    companion object {
        const val DEFAULT_DOCID_PREFIX = "PRODUCT"
    }

    fun <D: BaseDocument> similarityProduct(id: Long, doctype: Class<D>): List<Document> {
        val docIdPrefix = getDocIdPrefix(doctype)
        val idStr = generateUuid(docIdPrefix, id)
        return jdbcClient.query(getFormatterQuery(), getPreparedStatementSetter(idStr), documentRowMapper)
    }

    private fun getDocIdPrefix(docType: Class<*>?): String {
        return docType?.getAnnotation(DocumentMetadata::class.java)
            ?.docIdPrefix ?: DEFAULT_DOCID_PREFIX
    }

    private fun generateUuid(docIdPrefix: String, id: Long): UUID {
        return UUID.nameUUIDFromBytes("%s-%s".formatted(docIdPrefix, id).toByteArray())
    }

    private fun getPreparedStatementSetter(idStr: UUID): PreparedStatementSetter {
        return PreparedStatementSetter { ps ->
            StatementCreatorUtils.setParameterValue(ps, 1, Integer.MIN_VALUE, idStr)
            StatementCreatorUtils.setParameterValue(ps, 2, Integer.MIN_VALUE, idStr)
            StatementCreatorUtils.setParameterValue(ps, 3, Integer.MIN_VALUE,
                embeddingSearchConfiguration.similarityThreshold)
            StatementCreatorUtils.setParameterValue(ps, 4, Integer.MIN_VALUE,
                embeddingSearchConfiguration.topK)
        }
    }

    private fun getFormatterQuery(): String {
        return """
            WITH entity AS (
                    SELECT
                        id,
                        content,
                        metadata,
                        embedding
                    FROM
                        %s
                    WHERE
                        id = ?
                )
                SELECT
                    vs.id,
                    vs.content,
                    vs.metadata,
                    (vs.embedding <=> entity.embedding) AS similarity
                FROM
                    vector_store vs
                JOIN
                    entity ON true
                WHERE vs.id <> ? AND (vs.embedding <=> entity.embedding) > ?
                ORDER BY
                    similarity
                LIMIT ?
        """.formatted(vectorTableName)
    }
}
