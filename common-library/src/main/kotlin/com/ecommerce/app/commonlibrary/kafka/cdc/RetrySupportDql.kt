package com.ecommerce.app.commonlibrary.kafka.cdc

import org.springframework.core.annotation.AliasFor
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy
import org.springframework.retry.annotation.Backoff
import kotlin.reflect.KClass

/**
 * Custom annotation that extends Spring's [RetryableTopic] to
 * add retry and dead letter queue (DLQ) support for Kafka listeners.
 * Provides additional configuration for retry backoff, number of attempts,
 * topic creation, and exclusion of certain exceptions.
 */
@MustBeDocumented
@RetryableTopic
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RetrySupportDql(
    @get:AliasFor(annotation = RetryableTopic::class, attribute = "backoff")
    val backoff: Backoff = Backoff(6000),

    @get:AliasFor(annotation = RetryableTopic::class, attribute = "attempts")
    val attempts: String = "4",

    @get:AliasFor(annotation = RetryableTopic::class, attribute = "autoCreateTopics")
    val autoCreateTopics: String = "true",

    @get:AliasFor(annotation = RetryableTopic::class, attribute = "listenerContainerFactory")
    val listenerContainerFactory: String = "",

    @get:AliasFor(annotation = RetryableTopic::class, attribute = "exclude")
    val exclude: Array<KClass<out Throwable>> = [],

    @get:AliasFor(annotation = RetryableTopic::class, attribute = "sameIntervalTopicReuseStrategy")
    val sameIntervalTopicReuseStrategy: SameIntervalTopicReuseStrategy = SameIntervalTopicReuseStrategy.SINGLE_TOPIC
)
