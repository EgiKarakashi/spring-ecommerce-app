package com.ecommerce.app.order.service

import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.jvm.Throws

@Slf4j
abstract class AbstractCircuitBreakFallbackHandler {

    @Throws(Throwable::class)
    protected fun handleBodilessFallback(throwable: Throwable) {
        handleError(throwable)
    }

    @Throws(Throwable::class)
    protected fun <T> handleTypeFallback(throwable: Throwable): T? {
        handleError(throwable)
        return null
    }

    @Throws(Throwable::class)
    fun handleError(throwable: Throwable) {
        log.error("Circuit breaker records an error. Detail ${throwable.message}")
        throw  throwable
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(AbstractCircuitBreakFallbackHandler::class.java)
    }
}
