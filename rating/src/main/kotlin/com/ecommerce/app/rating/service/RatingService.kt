package com.ecommerce.app.rating.service

import com.ecommerce.app.commonlibrary.exception.AccessDeniedException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.commonlibrary.exception.ResourceExistedException
import com.ecommerce.app.rating.model.Rating
import com.ecommerce.app.rating.repository.RatingRepository
import com.ecommerce.app.rating.utils.AuthenticationUtils
import com.ecommerce.app.rating.utils.Constants
import com.ecommerce.app.rating.viewmodel.RatingListVm
import com.ecommerce.app.rating.viewmodel.RatingPostVm
import com.ecommerce.app.rating.viewmodel.RatingVm
import com.ecommerce.app.rating.viewmodel.ResponseStatusVm
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import org.springframework.util.ObjectUtils
import java.time.ZonedDateTime

@Service
@Transactional
class RatingService(
    private val ratingRepository: RatingRepository,
    private val customerService: CustomerService,
    private val orderService: OrderService
) {

    fun getRatingListWithFilter(
        productName: String,
        customerName: String,
        message: String,
        createdFrom: ZonedDateTime,
        createdTo: ZonedDateTime,
        pageNo: Int,
        pageSize: Int
    ): RatingListVm {
        val pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdOn").descending())
        val ratings = ratingRepository.getRatingListWithFilter(
            productName.lowercase(),
            customerName.lowercase(),
            message.lowercase(),
            createdFrom, createdTo,
            pageable
            )
        val ratingVmList = mutableListOf<RatingVm>()
        for (rating in ratings.content) {
            ratingVmList.add(RatingVm.fromModel(rating))
        }

        return RatingListVm(ratingVmList, ratings.totalElements, ratings.totalPages)
    }

    fun getLatestRatings(count: Int): List<RatingVm> {
        if (count <= 0) {
            return mutableListOf()
        }

        val pageable = PageRequest.of(0, count)
        val ratings = ratingRepository.getLatestRatings(pageable)

        if (CollectionUtils.isEmpty(ratings)) {
            return mutableListOf()
        }
        return ratings.stream()
            .map { RatingVm.fromModel(it) }
            .toList()
    }

    fun deleteRating(ratingId: Long): ResponseStatusVm {
        val rating = ratingRepository.findById(ratingId)
            .orElseThrow { NotFoundException(Constants.ErrorCode.RATING_NOT_FOUND, ratingId) }
        ratingRepository.delete(rating)
        return ResponseStatusVm("Delete Rating", Constants.Message.SUCCESS_MESSAGE, HttpStatus.OK.toString())
    }

    fun getRatingListByProductId(id: Long, pageNo: Int, pageSize: Int): RatingListVm {
        val pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdOn").descending())
        val ratings = ratingRepository.findByProductId(id, pageable)

        val ratingVmList = mutableListOf<RatingVm>()
        for (rating in ratings.content) {
            ratingVmList.add(RatingVm.fromModel(rating))
        }

        return RatingListVm(ratingVmList, ratings.totalElements, ratings.totalPages)
    }

    fun createRating(ratingPostVm: RatingPostVm): RatingVm {
        val userId = AuthenticationUtils.extractUserId()
        if (!orderService.checkOrderExistsByProductAndUserWithStatus(ratingPostVm.productId)!!.isPresent) {
            throw AccessDeniedException(Constants.ErrorCode.ACCESS_DENIED)
        }

        if (ratingRepository.existsByCreatedByAndProductId(userId, ratingPostVm.productId)) {
            throw ResourceExistedException(Constants.ErrorCode.RESOURCE_ALREADY_EXISTED)
        }

        val customerVm = customerService.getCustomer()
        if (customerVm == null) {
            throw NotFoundException(Constants.ErrorCode.CUSTOMER_NOT_FOUND, userId)
        }

        val rating = Rating().apply {
            ratingStar = ratingPostVm.star
            content = ratingPostVm.content
            productId = ratingPostVm.productId
            productName = ratingPostVm.productName
            lastName = customerVm.lastName
            firstName = customerVm.firstName
        }

        val savedRating = ratingRepository.save(rating)
        return RatingVm.fromModel(savedRating)

    }

    fun calculateAverageStar(productId: Long): Double {
        val totalStarsAndRatings = ratingRepository.getTotalStarsAndTotalRatings(productId)
        if (ObjectUtils.isEmpty(totalStarsAndRatings[0][0])) {
            return 0.0
        }
        val totalStars = totalStarsAndRatings[0][0].toString().toInt()
        val totalRatings = totalStarsAndRatings[0][1].toString().toInt()
        return (totalStars * 1.0) / totalRatings
    }
}
