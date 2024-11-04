package com.ecommerce.app.commonlibrary.kafka.cdc

import org.slf4j.LoggerFactory
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.MessageHeaders

abstract class BaseCdcConsumer<T> {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BaseCdcConsumer::class.java)
    }

    protected fun processMessage(record: T?, headers: MessageHeaders, consumer: (T) -> Unit) {
        LOGGER.debug("## Received message - headers: {}", headers)

        if (record == null) {
            LOGGER.warn("## Null payload received")
        } else {
            LOGGER.debug("## Processing record - Key {} | Value {}", headers[KafkaHeaders.RECEIVED_KEY], record)
            consumer(record)
            LOGGER.debug("## Record processed successfully - Key: {}", headers[KafkaHeaders.RECEIVED_KEY])
        }
    }
}
