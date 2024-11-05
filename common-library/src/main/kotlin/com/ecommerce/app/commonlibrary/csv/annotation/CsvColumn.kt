package com.ecommerce.app.commonlibrary.csv.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CsvColumn(val columnName: String = "")
