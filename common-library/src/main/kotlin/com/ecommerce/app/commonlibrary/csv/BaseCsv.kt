package com.ecommerce.app.commonlibrary.csv

import com.ecommerce.app.commonlibrary.csv.annotation.CsvColumn

open class BaseCsv(
    @CsvColumn(columnName = "Id")
    val id: Long?
)
