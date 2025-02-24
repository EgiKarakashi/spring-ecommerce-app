package com.ecommerce.app.commonlibrary.kafka.cdc.config

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
abstract class BaseKafkaListenerConfig<K : Any, V : Any>(
    private val keyType: Class<K>? = null,
    private val valueType: Class<V>? = null,
    private val kafkaProperties: KafkaProperties? = null
) {

    /**
     * Abstract method to provide a custom instance of [ConcurrentKafkaListenerContainerFactory].
     * (override method must be recognized as a bean)
     * If using the default one, get it from [kafkaListenerContainerFactory].
     *
     * @return a configured instance of [ConcurrentKafkaListenerContainerFactory].
     */
    abstract fun listenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<K, V>

    /**
     * Common instance type ConcurrentKafkaListenerContainerFactory.
     *
     * @return an instance of [ConcurrentKafkaListenerContainerFactory].
     */
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<K, V> {
        return ConcurrentKafkaListenerContainerFactory<K, V>().apply {
            consumerFactory = typeConsumerFactory(keyType, valueType)
        }
    }

    private fun typeConsumerFactory(keyClazz: Class<K>?, valueClazz: Class<V>?): ConsumerFactory<K, V> {
        val props = buildConsumerProperties()
        // Wrapper in case serialization/deserialization occurs
        val keyDeserializer = ErrorHandlingDeserializer(getJsonDeserializer(keyClazz!!))
        val valueDeserializer = ErrorHandlingDeserializer(getJsonDeserializer(valueClazz!!))
        return DefaultKafkaConsumerFactory(props, keyDeserializer, valueDeserializer)
    }

    private fun <T : Any> getJsonDeserializer(clazz: Class<T>): JsonDeserializer<T> {
        return JsonDeserializer(clazz).apply {
            addTrustedPackages("*")
        }
    }

    private fun buildConsumerProperties(): Map<String, Any> {
        return kafkaProperties!!.buildConsumerProperties(null)
    }
}

