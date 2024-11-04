package com.ecommerce.app.search.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@EnableElasticsearchRepositories(basePackages = ["com.ecommerce.app.search.repository"])
@ComponentScan(basePackages = ["com.ecommerce.app.search.service"])
class ImperativeClientConfig(
    private val elasticsearchConfig: ElasticsearchDataConfig
) : ElasticsearchConfiguration() {

    override fun clientConfiguration(): ClientConfiguration {
        return ClientConfiguration.builder()
            .connectedTo(elasticsearchConfig.url)
            .withBasicAuth(elasticsearchConfig.username, elasticsearchConfig.password)
            .build()
    }
}
