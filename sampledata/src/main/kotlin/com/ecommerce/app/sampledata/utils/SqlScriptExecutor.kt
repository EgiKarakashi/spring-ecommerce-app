package com.ecommerce.app.sampledata.utils

import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.init.ScriptUtils
import java.sql.SQLException
import javax.sql.DataSource

@Component
@Slf4j
class SqlScriptExecutor {
    companion object {
        private val log = LoggerFactory.getLogger(SqlScriptExecutor::class.java)
    }

    fun executeScriptsForSchema(dataSource: DataSource, schema: String, locationPattern: String) {
        val resolver = PathMatchingResourcePatternResolver()
        try {
            val resources = resolver.getResources(locationPattern)
            for (resource in resources) {
                executeSqlScript(dataSource, schema, resource)
            }
        } catch (e: Exception) {
            log.error(e.message)
        }
    }

    private fun executeSqlScript(dataSource: DataSource, schema: String, resource: Resource) {
        dataSource.connection.use { connection ->
            connection.schema = schema
            try {
                ScriptUtils.executeSqlScript(connection, resource)
                log.info("Executed script: ${resource.filename} on schema $schema")
            } catch (e: SQLException) {
                log.error(e.message)
            }
        }
    }
}
