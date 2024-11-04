package com.ecommerce.app.search.kafka.config.consumer

import com.ecommerce.app.commonlibrary.kafka.cdc.config.BaseKafkaListenerConfig
import com.ecommerce.app.commonlibrary.kafka.cdc.message.ProductCdcMessage
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory

@EnableKafka
@Configuration
class ProductCdcKafkaListenerConfig(
    kafkaProperties: KafkaProperties?
) : BaseKafkaListenerConfig<ProductCdcMessage>(ProductCdcMessage::class.java, kafkaProperties) {

    @Bean(name = [PRODUCT_CDC_LISTENER_CONTAINER_FACTORY])
    override fun listenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, ProductCdcMessage> {
        return super.kafkaListenerContainerFactory()
    }

    companion object {
        const val PRODUCT_CDC_LISTENER_CONTAINER_FACTORY = "productCdcContainerFactory"
    }
}
