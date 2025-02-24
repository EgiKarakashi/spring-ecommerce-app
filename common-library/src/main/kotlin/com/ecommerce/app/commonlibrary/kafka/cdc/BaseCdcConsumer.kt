package com.ecommerce.app.commonlibrary.kafka.cdc

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.MessageHeaders
import java.util.function.BiConsumer
import java.util.function.Consumer

abstract class BaseCdcConsumer<K, V> {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(BaseCdcConsumer::class.java)
        private const val RECEIVED_MESSAGE_HEADERS = "## Received message - headers: {}"
        private const val PROCESSING_RECORD_KEY_VALUE = "## Processing record - Key: {} | Value: {}"
        private const val RECORD_PROCESSED_SUCCESSFULLY_KEY = "## Record processed successfully - Key: {} \n"
    }

    protected fun processMessage(record: V, headers: MessageHeaders, consumer: Consumer<V>) {
        LOGGER.debug(RECEIVED_MESSAGE_HEADERS, headers)
        LOGGER.debug(PROCESSING_RECORD_KEY_VALUE, headers[KafkaHeaders.RECEIVED_KEY], record)
        consumer.accept(record)
        LOGGER.debug(RECORD_PROCESSED_SUCCESSFULLY_KEY, headers[KafkaHeaders.RECEIVED_KEY])
    }

    protected fun processMessage(key: K, value: V, headers: MessageHeaders, consumer: BiConsumer<K, V>) {
        LOGGER.debug(RECEIVED_MESSAGE_HEADERS, headers)
        LOGGER.debug(PROCESSING_RECORD_KEY_VALUE, key, value)
        consumer.accept(key, value)
        LOGGER.debug(RECORD_PROCESSED_SUCCESSFULLY_KEY, key)
    }
}
