package com.ecommerce.app.order.service

import com.ecommerce.app.order.config.ServiceUrlConfig
import com.ecommerce.app.order.viewmodel.promotion.PromotionUsageVm
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class PromotionService(
    val restClient: RestClient,
    val serviceUrlConfig: ServiceUrlConfig
): AbstractCircuitBreakFallbackHandler() {

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleBodilessFallback")
    fun updateUsagePromotion(promotionUsageVms: List<PromotionUsageVm>) {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.promotion!!)
            .path("/backoffice/promotions/updateUsage")
            .buildAndExpand()
            .toUri()

        restClient.post()
            .uri(url)
            .headers { h -> h.setBearerAuth(jwt) }
            .body(promotionUsageVms)
            .retrieve()
    }
}
