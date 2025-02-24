package com.ecommerce.app.springecommerceapp.kafka.consumer

import com.ecommerce.app.commonlibrary.kafka.cdc.BaseCdcConsumer
import com.ecommerce.app.commonlibrary.kafka.cdc.RetrySupportDql
import com.ecommerce.app.commonlibrary.kafka.cdc.message.ProductCdcMessage
import com.ecommerce.app.commonlibrary.kafka.cdc.message.ProductMsgKey
import com.ecommerce.app.springecommerceapp.kafka.config.consumer.ProductCdcKafkaListenerConfig.Companion.PRODUCT_CDC_LISTENER_CONTAINER_FACTORY
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class ProductSyncDataConsumer: BaseCdcConsumer<ProductMsgKey, ProductCdcMessage>() {

    @Autowired
    private lateinit var productSyncService: ProductSyncService

    @KafkaListener(
        id = "product-sync-recommendation",
        groupId = "product-sync",
        topics = ["\${product.topic.name}"],
        containerFactory = PRODUCT_CDC_LISTENER_CONTAINER_FACTORY
    )
    @RetrySupportDql(listenerContainerFactory = PRODUCT_CDC_LISTENER_CONTAINER_FACTORY)
    fun processMessage(
        @Header(KafkaHeaders.RECEIVED_KEY) key: ProductMsgKey,
        @Payload(required = false) productCdcMessage: ProductCdcMessage,
        @Headers headers: MessageHeaders
    ) {
        processMessage(key, productCdcMessage, headers, productSyncService::sync)
    }
}
