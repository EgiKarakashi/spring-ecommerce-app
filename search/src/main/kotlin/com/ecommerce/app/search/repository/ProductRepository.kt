package com.ecommerce.app.search.repository

import com.ecommerce.app.search.model.Product
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ProductRepository: ElasticsearchRepository<Product, Long> {
}
