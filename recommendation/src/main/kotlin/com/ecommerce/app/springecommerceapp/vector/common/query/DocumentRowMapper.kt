package com.ecommerce.app.springecommerceapp.vector.common.query

import com.fasterxml.jackson.databind.ObjectMapper
import lombok.SneakyThrows
import org.springframework.ai.document.Document
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.sql.SQLException

@Component
class DocumentRowMapper(private val objectMapper: ObjectMapper): RowMapper<Document> {
    companion object {
        const val ID = "id"
        const val CONTENT = "content"
        const val METADATA = "metadata"
    }

    @SneakyThrows(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): Document? {
        val id = rs.getString(ID) as String
        val content = rs.getString(CONTENT) as String
        val metadata = objectMapper.readValue(rs.getObject(METADATA).toString(), Map::class.java) as Map<String, Any>
        return Document(id, content, metadata)
    }
}
