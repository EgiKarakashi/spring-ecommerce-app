package com.ecommerce.app.commonlibrary.csv

import com.ecommerce.app.commonlibrary.csv.annotation.CsvColumn
import com.ecommerce.app.commonlibrary.csv.annotation.CsvName
import com.ecommerce.app.commonlibrary.utils.DateTimeUtils
import com.opencsv.CSVWriter
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@Slf4j
object CsvExporter {

    private const val GET_PREFIX = "get"

    @JvmStatic
    fun <T> exportToCsv(dataList: List<BaseCsv>, clazz: Class<T>): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8).use { outputStreamWriter ->
                CSVWriter(outputStreamWriter, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END).use { csvWriter ->

                    // Write CSV header
                    writeCsvHeader(csvWriter, clazz)

                    // Write CSV data
                    writeCsvData(csvWriter, dataList, clazz)

                    csvWriter.flush()
                    return byteArrayOutputStream.toByteArray()
                }
            }
        }
    }

    private fun <T> writeCsvHeader(csvWriter: CSVWriter, clazz: Class<T>) {
        val baseFields = BaseCsv::class.java.declaredFields
        val fields = clazz.declaredFields

        val header = (baseFields.asSequence() + fields.asSequence())
            .filter { it.getAnnotation(CsvColumn::class.java) != null }
            .map { it.getAnnotation(CsvColumn::class.java).columnName }
            .toList()
            .toTypedArray()

        csvWriter.writeNext(header)
    }

    private fun <T> writeCsvData(csvWriter: CSVWriter, dataList: List<BaseCsv>, clazz: Class<T>) {
        val baseFields = BaseCsv::class.java.declaredFields
        val fields = clazz.declaredFields

        val allFields = (baseFields.asSequence() + fields.asSequence())
            .filter { it.getAnnotation(CsvColumn::class.java) != null }
            .toList()
            .toTypedArray()

        dataList.forEach { data ->
            val row = getFieldValues(allFields, data)
            csvWriter.writeNext(row)
        }
    }

    private fun getFieldValues(fields: Array<Field>, data: Any): Array<String> {
        return fields
            .filter { it.getAnnotation(CsvColumn::class.java) != null }
            .map { getFieldValueAsString(it, data) }
            .toTypedArray()
    }

    private fun getFieldValueAsString(field: Field, data: Any): String {
        return try {
            val getterName = GET_PREFIX + field.name.capitalize()
            val getter: Method = data.javaClass.getMethod(getterName)
            val value = getter.invoke(data)

            if (value is List<*>) {
                return "[" + value.joinToString("|") + "]"
            }

            value?.toString() ?: ""
        } catch (e: IllegalAccessException) {
            log.warn("Get value field err {}", e.message)
            ""
        } catch (e: NoSuchMethodException) {
            log.warn("Get value err {}", e.message)
            ""
        } catch (e: InvocationTargetException) {
            log.warn("Invocation target err {}", e.message)
            ""
        }
    }

    @JvmStatic
    fun <T> createFileName(clazz: Class<T>): String {
        val fromDate = DateTimeUtils.format(LocalDateTime.now())
        val csvName = clazz.getAnnotation(CsvName::class.java)
        return "${csvName.fileName}_$fromDate.csv"
    }

    val log = LoggerFactory.getLogger(CsvExporter::class.java)
}
