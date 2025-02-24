package com.ecommerce.app.search.kafka.consumer

import com.ecommerce.app.commonlibrary.kafka.cdc.BaseCdcConsumer
import com.ecommerce.app.commonlibrary.kafka.cdc.message.Operation
import com.ecommerce.app.commonlibrary.kafka.cdc.message.ProductCdcMessage
import com.ecommerce.app.commonlibrary.kafka.cdc.message.ProductMsgKey
import com.ecommerce.app.search.kafka.config.consumer.ProductCdcKafkaListenerConfig.Companion.PRODUCT_CDC_LISTENER_CONTAINER_FACTORY
import com.ecommerce.app.search.service.ProductSyncDataService
import jakarta.validation.Valid
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Slf4j
@Service
class ProductSyncDataConsumer(
  val productSyncDataService: ProductSyncDataService
): BaseCdcConsumer<ProductMsgKey,ProductCdcMessage>() {

    @KafkaListener(
        id = "product-sync-es",
        groupId = "product-sync-search",
        topics = ["\${product.topic.name}"],
        containerFactory = PRODUCT_CDC_LISTENER_CONTAINER_FACTORY
    )
    fun processMessage(
        @Header(KafkaHeaders.RECEIVED_KEY) key: ProductMsgKey,
        @Payload(required  = false) @Valid productCdcMessage: ProductCdcMessage,
        @Headers headers: MessageHeaders
    ) {
        log.info("Received message with key: $key and payload: $productCdcMessage")
        processMessage(key, productCdcMessage, headers, this::sync)
    }

    fun sync(productMsgKey: ProductMsgKey, productCdcMessage: ProductCdcMessage?) {
        val productId = productMsgKey.id
        val isHardDeleteEvent = productCdcMessage == null || Operation.DELETE.equals(productCdcMessage.op)

        try {
            if (isHardDeleteEvent) {
                log.warn("Hard delete event for product: $productId")
                productSyncDataService.deleteProduct(productId)
            } else {
                when (productCdcMessage?.op) {
                    Operation.CREATE, Operation.READ -> {
                        log.info("Creating product: $productId")
                        productSyncDataService.createProduct(productId)
                    }
                    Operation.UPDATE -> {
                        log.info("Updating product: $productId")
                        productSyncDataService.updateProduct(productId)
                    }
                    else -> log.warn("Unsupported operation: ${productCdcMessage?.op} for product: $productId")
                }
            }
        } catch (e: Exception) {
            log.error("Failed to process product: $productId, error: ${e.message}", e)
        }
    }


    companion object {
        val log = LoggerFactory.getLogger(ProductSyncDataService::class.java)
    }
}
