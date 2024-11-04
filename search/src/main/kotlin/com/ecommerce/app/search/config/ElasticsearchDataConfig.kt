package com.ecommerce.app.search.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@ConfigurationProperties(prefix = "elasticsearch")
@Configuration
class ElasticsearchDataConfig {
    @Value("\${elasticsearch.url}")
    val url: String = ""
    @Value("\${elasticsearch.username}")
    val username: String = ""
    @Value("\${elasticsearch.password}")
    val password: String = ""
}
