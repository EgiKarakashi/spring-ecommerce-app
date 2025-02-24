package com.ecommerce.app.cart.service

import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory

@Slf4j
abstract class AbstractCircuitBreakFallbackHandler {

    @Throws(Throwable::class)
    protected fun handleBodilessFallback(throwable: Throwable) {
        handleError(throwable)
    }

    open fun <T> handleTypedFallback(throwable: Throwable): T? {
        handleError(throwable)
        return null
    }

    @Throws(Throwable::class)
    private fun handleError(throwable: Throwable) {
        log.error("Circuit breaker records an error. Detail ${throwable.message}")
    }

    companion object {
        val log = LoggerFactory.getLogger(AbstractCircuitBreakFallbackHandler::class.java)
    }
}
