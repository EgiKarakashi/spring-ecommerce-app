package com.ecommerce.app.search.kafka.consumer

import com.ecommerce.app.commonlibrary.kafka.cdc.BaseCdcConsumer
import com.ecommerce.app.commonlibrary.kafka.cdc.message.Operation
import com.ecommerce.app.commonlibrary.kafka.cdc.message.ProductCdcMessage
import com.ecommerce.app.search.kafka.config.consumer.ProductCdcKafkaListenerConfig.Companion.PRODUCT_CDC_LISTENER_CONTAINER_FACTORY
import com.ecommerce.app.search.service.ProductSyncDataService
import jakarta.validation.Valid
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Slf4j
@Service
class ProductSyncDataConsumer(
  val productSyncDataService: ProductSyncDataService
): BaseCdcConsumer<ProductCdcMessage>() {

    @KafkaListener(
        id = "product-sync-es",
        groupId = "product-sync-search",
        topics = ["product-topic-name"],
        containerFactory = PRODUCT_CDC_LISTENER_CONTAINER_FACTORY
    )
    fun processMessage(
        @Payload(required  = false) @Valid productCdcMessage: ProductCdcMessage,
        @Headers headers: MessageHeaders
    ) {
        processMessage(productCdcMessage, headers, this::sync)
    }

    fun sync(productCdcMessage: ProductCdcMessage) {
        if (productCdcMessage.after != null) {
            val operation = productCdcMessage.op
            val productId = productCdcMessage.after.id
            when (operation) {
                Operation.CREATE, Operation.READ -> productSyncDataService.createProduct(productId)
                Operation.UPDATE -> productSyncDataService.updateProduct(productId)
                else -> log.warn("Unsupported operation '{}' for product: '{}'", operation, productId)
            }
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(ProductSyncDataService::class.java)
    }
}
