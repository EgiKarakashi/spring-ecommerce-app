package com.ecommerce.app.search.model

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting
import java.time.ZonedDateTime

@Document(indexName = "product")
@Setting(settingPath = "esconfig/elastic-analyzer.json")
data class Product(
    @Id
    val id: Long,
    @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
    var name: String,
    var slug: String,
    @Field(type = FieldType.Double)
    var price: Double,
    var isPublished: Boolean,
    var isVisibleIndividually: Boolean,
    var isAllowedToOrder: Boolean,
    var isFeatured: Boolean,
    var thumbnailMediaId: Long,
    @Field(type = FieldType.Text, fielddata = true)
    var brand: String,
    @Field(type = FieldType.Keyword)
    var categories: List<String>,
    @Field(type = FieldType.Keyword)
    var attributes: List<String>,
    @Field(type = FieldType.Date)
    val createdOn: ZonedDateTime?
)
