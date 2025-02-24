package com.ecommerce.app.rating.controller

import com.ecommerce.app.rating.service.RatingService
import com.ecommerce.app.rating.viewmodel.RatingListVm
import com.ecommerce.app.rating.viewmodel.RatingPostVm
import com.ecommerce.app.rating.viewmodel.RatingVm
import com.ecommerce.app.rating.viewmodel.ResponseStatusVm
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
class RatingController(private val ratingService: RatingService) {

    @GetMapping("/backoffice/ratings")
    fun getRatingListWithFilter(
        @RequestParam(value = "productName", defaultValue = "", required = false) productName: String,
        @RequestParam(value = "customerName", defaultValue = "", required = false) customerName: String,
        @RequestParam(value = "message", defaultValue = "", required = false) message: String,
        @RequestParam(value = "createdFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) createdFrom: ZonedDateTime?,
        @RequestParam(
            value = "createdTo",
            defaultValue = "#{T(java.time.ZonedDateTime).now().toString()}",
            required = false
        ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) createdTo: ZonedDateTime,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) pageSize: Int
    ): ResponseEntity<RatingListVm> {
        val effectiveCreatedFrom = createdFrom ?: ZonedDateTime.now().minusMonths(1)
        return ResponseEntity.ok(ratingService.getRatingListWithFilter(productName, customerName, message, effectiveCreatedFrom, createdTo, pageNo, pageSize))
    }

    @GetMapping("/backoffice/ratings/latest/{count}")
    fun getLatestRatings(@PathVariable count: Int): ResponseEntity<List<RatingVm>> {
        return ResponseEntity.ok(ratingService.getLatestRatings(count))
    }

    @DeleteMapping("/backoffice/ratings/{id}")
    fun deleteRating(@PathVariable id: Long): ResponseEntity<ResponseStatusVm> {
        return ResponseEntity.ok(ratingService.deleteRating(id))
    }

    @GetMapping("/storefront/ratings/products/{productId}")
    fun getRatingList(
        @PathVariable productId: Long,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) pageNo: Int,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) pageSize: Int
    ): ResponseEntity<RatingListVm> {
        return ResponseEntity.ok(ratingService.getRatingListByProductId(productId, pageNo, pageSize))
    }

    @PostMapping("/storefront/ratings")
    fun createRating(@Valid @RequestBody ratingPostVm: RatingPostVm): ResponseEntity<RatingVm> {
        return ResponseEntity.ok(ratingService.createRating(ratingPostVm))
    }

    @GetMapping("/storefront/ratings/product/{productId}/average-star")
    fun getAverageStarOfProduct(@PathVariable productId: Long): Double {
        return ratingService.calculateAverageStar(productId)
    }
}
