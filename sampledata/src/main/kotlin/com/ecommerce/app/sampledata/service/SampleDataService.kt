package com.ecommerce.app.sampledata.service

import com.ecommerce.app.sampledata.utils.SqlScriptExecutor
import com.ecommerce.app.sampledata.viewmodel.SampleDataVm
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.sql.DataSource

@Service
@Transactional
class SampleDataService(
    val productDataSource: DataSource,
    val mediaDataSource: DataSource
) {

    fun createSampleData(): SampleDataVm {
        val executor = SqlScriptExecutor()
        executor.executeScriptsForSchema(productDataSource, "public", "classpath*:db/product/*.sql")
        executor.executeScriptsForSchema(mediaDataSource, "public", "classpath*:db/media/*.sql")
        return SampleDataVm("Insert Sample data successfully")
    }
}
