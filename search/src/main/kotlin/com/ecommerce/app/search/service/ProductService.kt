package com.ecommerce.app.search.service

import com.ecommerce.app.search.model.ProductCriteriaDto
import com.ecommerce.app.search.viewmodel.ProductListGetVm
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import com.ecommerce.app.search.constant.ProductField
import com.ecommerce.app.search.constant.enums.SortType
import com.ecommerce.app.search.model.Product
import com.ecommerce.app.search.viewmodel.ProductGetVm
import com.ecommerce.app.search.viewmodel.ProductNameGetVm
import com.ecommerce.app.search.viewmodel.ProductNameListVm
import org.elasticsearch.common.unit.Fuzziness
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.*
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val elasticsearchOperations: ElasticsearchOperations
) {

    fun findProductAdvance(productCriteria: ProductCriteriaDto): ProductListGetVm {
//        val nativeQuery = NativeQuery.builder()
//            .withAggregation("categories", Aggregation.of { a ->
//                a.terms { ta -> ta.field(ProductField.CATEGORIES) }
//            })
//            .withAggregation("attributes", Aggregation.of { a ->
//                a.terms { ta -> ta.field(ProductField.ATTRIBUTES) }
//            })
//            .withAggregation("brands", Aggregation.of { a ->
//                a.terms { ta -> ta.field(ProductField.BRAND) }
//            })
//            .withQuery { q ->
//                q.bool { b ->
//                    b.should { s ->
//                        s.multiMatch { m ->
//                            m.fields(ProductField.NAME, ProductField.BRAND, ProductField.CATEGORIES)
//                                .query(productCriteria.keyword)
//                                .fuzziness(Fuzziness.ONE.asString())
//                        }
//                    }
//                }
//            }
//            .withPageable(PageRequest.of(productCriteria.page, productCriteria.size))

        val nativeQuery = NativeQuery.builder()
            .withQuery { q ->
                q.bool { b ->
                    b.should { s ->
                        s.multiMatch { m ->
                            m.fields(ProductField.NAME, ProductField.BRAND, ProductField.CATEGORIES)
                                .query(productCriteria.keyword)
                                .fuzziness(Fuzziness.ONE.asString())
                        }
                    }
                }
            }
            .withPageable(PageRequest.of(productCriteria.page, productCriteria.size))


        nativeQuery.withFilter { f ->
            f.bool { b ->
                extractedTermsFilter(productCriteria.brand, ProductField.BRAND, b)
                extractedTermsFilter(productCriteria.category, ProductField.CATEGORIES, b)
                extractedTermsFilter(productCriteria.attribute, ProductField.ATTRIBUTES, b)
                extractedRange(productCriteria.minPrice, productCriteria.maxPrice, b)
                b.must { m -> m.term { t -> t.field(ProductField.IS_PUBLISHED).value(true) } }
                b
            }
        }

        when (productCriteria.sortType) {
            SortType.PRICE_ASC -> nativeQuery.withSort(Sort.by(Sort.Direction.ASC, ProductField.PRICE))
            SortType.PRICE_DESC -> nativeQuery.withSort(Sort.by(Sort.Direction.DESC, ProductField.PRICE))
            else -> nativeQuery.withSort(Sort.by(Sort.Direction.DESC, ProductField.CREATE_ON))
        }

        val searchHitsResult: SearchHits<Product>

        try {
            searchHitsResult = elasticsearchOperations.search(nativeQuery.build(), Product::class.java)
        } catch (e: UncategorizedElasticsearchException) {
            println("Elasticsearch error: ${e.cause?.message}")
            throw e
        }

        val productPage: SearchPage<Product> = SearchHitSupport.searchPageFor(searchHitsResult, nativeQuery.pageable)

        val productListVmList: List<ProductGetVm> = searchHitsResult.searchHits
            .map { i -> ProductGetVm.fromModel(i.content) }

        return ProductListGetVm(
            productListVmList,
            productPage.number,
            productPage.size,
            productPage.totalElements,
            productPage.totalPages,
            productPage.isLast,
            getAggregations(searchHitsResult)
        )

    }

    private fun extractedTermsFilter(fieldValues: String?, productField: String, b: BoolQuery.Builder) {
        if (fieldValues.isNullOrBlank()) {
            return
        }
        val valuesArray = fieldValues.split(",")
        b.must { m ->
            val innerBool = BoolQuery.Builder()
            valuesArray.forEach { value ->
                innerBool.should { s ->
                    s.term { t ->
                        t.field(productField)
                            .value(value)
                            .caseInsensitive(true)
                    }
                }
            }
            Query.Builder().bool(innerBool.build())
        }
    }

    private fun extractedRange(min: Number?, max: Number?, bool: BoolQuery.Builder) {
        if (min != null || max != null) {
            bool.must { m ->
                m.range { r ->
                    r.field(ProductField.PRICE)
                        .from(min?.toString())
                        .to(max?.toString())
                }
            }
        }
    }

    private fun getAggregations(searchHits: SearchHits<Product>): Map<String, Map<String, Long>> {
        val aggregations = mutableListOf<org.springframework.data.elasticsearch.client.elc.Aggregation>()

        if (searchHits.hasAggregations()) {
            (searchHits.aggregations?.aggregations() as? List<ElasticsearchAggregation>)?.forEach { elsAgg ->
                aggregations.add(elsAgg.aggregation())
            }
        }

        val aggregationsMap = mutableMapOf<String, MutableMap<String, Long>>()
        aggregations.forEach { agg ->
            val aggregation = mutableMapOf<String, Long>()
            val stringTermsAggregate = agg.aggregate._get() as StringTermsAggregate
            val stringTermsBuckets = stringTermsAggregate.buckets()._get() as List<StringTermsBucket>

            stringTermsBuckets.forEach { bucket ->
                aggregation[bucket.key()._get().toString()] = bucket.docCount()
            }

            aggregationsMap[agg.name] = aggregation
        }

        return aggregationsMap
    }

    fun autoCompleteProductName(keyword: String): ProductNameListVm {
        val matchQuery = NativeQuery.builder()
            .withQuery { q ->
                q.matchPhrasePrefix { matchPhrasePrefix ->
                    matchPhrasePrefix.field("name").query(keyword)
                }
            }
            .withSourceFilter(FetchSourceFilter(arrayOf("name"), null))
            .build()

        val result: SearchHits<Product> = elasticsearchOperations.search(matchQuery, Product::class.java)
        val products: List<Product> = result.stream().map { it.content }.toList()

        return ProductNameListVm(products.map { ProductNameGetVm.fromModel(it) })
    }

}
