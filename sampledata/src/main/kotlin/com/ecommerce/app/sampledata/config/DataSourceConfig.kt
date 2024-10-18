package com.ecommerce.app.sampledata.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
@ConfigurationPropertiesScan
class DataSourceConfig(
    @Value("\${spring.datasource.product.url}")
    val productUrl: String,

    @Value("\${spring.datasource.media.url}")
    val mediaUrl: String,

    @Value("\${spring.datasource.password}")
    val password: String,

    @Value("\${spring.datasource.username}")
    val username: String,

    @Value("\${spring.datasource.driver-class-name}")
    val driveClassName: String
) {

    @Bean(name = ["productDataSource"])
    @Primary
    fun productDataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName(driveClassName)
            .url(productUrl)
            .username(username)
            .password(password)
            .build()
    }

    @Bean(name = ["mediaDataSource"])
    fun mediaDataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName(driveClassName)
            .url(mediaUrl)
            .username(username)
            .password(password)
            .build()
    }

    @Primary
    @Bean(name = ["jdbcProduct"])
    fun jdbcProduct(@Qualifier("productDataSource") productDataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(productDataSource)
    }

    @Bean(name = ["jdbcMedia"])
    fun jdbcMedia(@Qualifier("mediaDataSource") mediaDataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(mediaDataSource)
    }
}
