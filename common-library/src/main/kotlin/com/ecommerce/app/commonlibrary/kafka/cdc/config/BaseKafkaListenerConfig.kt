package com.ecommerce.app.commonlibrary.kafka.cdc.config

import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

/**
 * Base configuration class for setting up Kafka consumers with typed deserialization.
 *
 * @param T The type of messages consumed.
 */
abstract class BaseKafkaListenerConfig<T>(
    private val type: Class<T>,
    private val kafkaProperties: KafkaProperties?
) {

    /**
     * Abstract method to provide a custom instance of [ConcurrentKafkaListenerContainerFactory].
     * Override method must be recognized as a bean.
     * In case a default instance is needed, use [kafkaListenerContainerFactory].
     *
     * @return a configured instance of [ConcurrentKafkaListenerContainerFactory].
     */
    abstract fun listenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, T>

    /**
     * Provides a common instance of [ConcurrentKafkaListenerContainerFactory].
     *
     * @return an instance of [ConcurrentKafkaListenerContainerFactory].
     */
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, T> {
        return ConcurrentKafkaListenerContainerFactory<String, T>().apply {
            consumerFactory = typeConsumerFactory(type)
        }
    }

    private fun typeConsumerFactory(clazz: Class<T>): ConsumerFactory<String, T> {
        val props = buildConsumerProperties()
        val stringDeserializer = StringDeserializer()
        val jsonDeserializer = JsonDeserializer(clazz).apply {
            addTrustedPackages("*")
        }
        val errorHandlingDeserializer = ErrorHandlingDeserializer(jsonDeserializer)
        return DefaultKafkaConsumerFactory(props, stringDeserializer, errorHandlingDeserializer)
    }

    private fun buildConsumerProperties(): Map<String, Any> {
        return kafkaProperties!!.buildConsumerProperties()
    }
}
