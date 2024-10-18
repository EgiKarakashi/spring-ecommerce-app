package com.ecommerce.app.product.service

import com.ecommerce.app.product.config.ServiceUrlConfig
import com.ecommerce.app.product.viewmodel.NoFileMediaVm
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springdoc.core.service.RequestBodyService
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import kotlin.jvm.Throws

@Service
class MediaService(
    private val restClient: RestClient,
    private val serviceUrlConfig: ServiceUrlConfig,
): AbstractCircuitBreakFallbackHandler() {

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleMediaFallback")
    fun saveFile(multipartFile: MultipartFile, caption: String, fileNameOverride: String): NoFileMediaVm? {
        val url: URI = UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.media).path("/medias").build().toUri()
        val jwt: String = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue

        val builder = MultipartBodyBuilder()
        builder.part("multipartFile", multipartFile.resource)
        builder.part("caption,", caption)
        builder.part("fileNameOverride", fileNameOverride)

        return restClient.post()
            .uri(url)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .headers { h -> h.setBearerAuth(jwt) }
            .body(builder.build())
            .retrieve()
            .body(NoFileMediaVm::class.java)
    }

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleMediaFallback")
    fun getMedia(id: Long?): NoFileMediaVm? {
        if (id == null) {
            return NoFileMediaVm(null, "", "", "", "")
        }
        val url: URI = UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.media)
            .path("/{id}").buildAndExpand(id).toUri()
        return restClient.get()
            .uri(url)
            .retrieve()
            .body(NoFileMediaVm::class.java)
    }

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleMediaFallback")
    fun removeMedia(id: Long) {
        val url: URI =  UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.media).path("/media/{id}")
            .buildAndExpand(id).toUri()
        val jwt: String = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        restClient.delete()
            .uri(url)
            .headers { h -> h.setBearerAuth(jwt) }
            .retrieve()
            .body(Unit::class.java)
    }

    @Throws(Throwable::class)
    private fun handleMediaFallback(throwable: Throwable): NoFileMediaVm? {
        return handleTypeFallback(throwable)
    }
}
