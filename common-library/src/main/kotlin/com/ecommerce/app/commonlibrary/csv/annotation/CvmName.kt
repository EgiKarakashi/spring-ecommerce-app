package com.ecommerce.app.commonlibrary.csv.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CsvName(val fileName: String = "")
