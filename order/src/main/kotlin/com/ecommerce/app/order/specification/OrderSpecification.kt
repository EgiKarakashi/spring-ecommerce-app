package com.ecommerce.app.order.specification

import com.ecommerce.app.order.model.Order
import com.ecommerce.app.order.model.OrderItem
import com.ecommerce.app.order.model.enumeration.OrderStatus
import com.ecommerce.app.order.utils.Constants
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Root
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.data.jpa.domain.Specification
import java.time.ZonedDateTime
import java.util.*

object OrderSpecification {
    fun existsByCreatedByAndInProductIdAndOrderStatusCompleted(
        createdBy: String?, productIds: List<Long?>?
    ): Specification<Order> {
        return Specification.where(hasCreatedBy(createdBy))
            .and(hasOrderStatus(OrderStatus.COMPLETED))
            .and(hasProductInOrderItems(productIds))
    }

    fun findMyOrders(userId: String?, productName: String, orderStatus: OrderStatus?): Specification<Order> {
        return Specification { root: Root<Order>?, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            val hasCreatedByPredicate = hasCreatedBy(userId).toPredicate(root!!, query, criteriaBuilder)
            val hasOrderStatusPredicate = hasOrderStatus(orderStatus).toPredicate(root, query, criteriaBuilder)
            val hasProductNamePredicate = hasProductNameInOrderItems(productName).toPredicate(
                root, query, criteriaBuilder
            )
            criteriaBuilder.and(
                hasCreatedByPredicate,
                hasOrderStatusPredicate,
                hasProductNamePredicate
            )
        }
    }

    fun findOrderByWithMulCriteria(
        orderStatus: List<OrderStatus?>?,
        billingPhoneNumber: String?,
        countryName: String?,
        email: String,
        productName: String,
        createdFrom: ZonedDateTime?,
        createdTo: ZonedDateTime?
    ): Specification<Order?> {
        return Specification { root: Root<Order?>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (query != null && Long::class.java != query.resultType) {
                root.fetch<Any, Any>(Constants.Column.ORDER_SHIPPING_ADDRESS_ID_COLUMN, JoinType.LEFT)
                root.fetch<Any, Any>(Constants.Column.ORDER_BILLING_ADDRESS_ID_COLUMN, JoinType.LEFT)
            }
            val withEmail = withEmail(email).toPredicate(root, query, criteriaBuilder)
            val withOrderStatus = withOrderStatus(orderStatus).toPredicate(root, query, criteriaBuilder)
            val withProductName = withProductName(productName).toPredicate(root, query, criteriaBuilder)
            val withBillingPhoneNumber =
                withBillingPhoneNumber(billingPhoneNumber).toPredicate(root, query, criteriaBuilder)
            val withCountryName = withCountryName(countryName).toPredicate(root, query, criteriaBuilder)
            val withDateRange = withDateRange(createdFrom, createdTo).toPredicate(root, query, criteriaBuilder)
            criteriaBuilder.and(
                withEmail,
                withOrderStatus,
                withProductName,
                withBillingPhoneNumber,
                withCountryName,
                withDateRange
            )
        }
    }

    private fun hasProductInOrderItems(productIds: List<Long?>?): Specification<Order> {
        return Specification { root: Root<Order>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (query == null) {
                return@Specification criteriaBuilder.conjunction()
            }
            val subquery = query.subquery(OrderItem::class.java)
            val orderItemRoot = subquery.from(OrderItem::class.java)
            subquery.select(orderItemRoot)
                .where(
                    criteriaBuilder.and(
                        criteriaBuilder.equal(
                            orderItemRoot.get<Any>(Constants.Column.ORDER_ORDER_ID_COLUMN),
                            root.get<Any>(Constants.Column.ID_COLUMN)
                        ),
                        orderItemRoot.get<Any>(Constants.Column.ORDER_ITEM_PRODUCT_ID_COLUMN)
                            .`in`(Optional.ofNullable(productIds).orElse(listOf<Long>()))
                    )
                )
            criteriaBuilder.exists(subquery)
        }
    }

    private fun hasCreatedBy(createdBy: String?): Specification<Order> {
        return Specification { root: Root<Order>, _: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            criteriaBuilder.equal(
                root.get<Any>(
                    Constants.Column.CREATE_BY_COLUMN
                ), createdBy
            )
        }
    }

    private fun hasOrderStatus(orderStatus: OrderStatus?): Specification<Order> {
        return Specification { root: Root<Order>, _: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (orderStatus != null) {
                return@Specification criteriaBuilder.equal(
                    root.get<Any>(Constants.Column.ORDER_ORDER_STATUS_COLUMN),
                    orderStatus
                )
            }
            criteriaBuilder.conjunction()
        }
    }

    private fun hasProductNameInOrderItems(productName: String): Specification<Order> {
        return Specification { root: Root<Order>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (query == null) {
                return@Specification criteriaBuilder.conjunction()
            }
            val subquery = query.subquery(Long::class.java)
            val orderItemRoot = subquery.from(OrderItem::class.java)

            var productNamePredicate = criteriaBuilder.conjunction()
            if (StringUtils.isNotEmpty(productName)) {
                productNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(orderItemRoot.get(Constants.Column.ORDER_ITEM_PRODUCT_NAME_COLUMN)),
                    "%" + productName.lowercase(Locale.getDefault()) + "%"
                )
            }

            subquery.select(orderItemRoot.get(Constants.Column.ORDER_ORDER_ID_COLUMN))
                .where(productNamePredicate)
            criteriaBuilder.`in`(root.get<Any>(Constants.Column.ID_COLUMN)).value(subquery)
        }
    }

    private fun withEmail(email: String): Specification<Order?> {
        return Specification { root: Root<Order?>, _: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (StringUtils.isNotEmpty(email)) {
                return@Specification criteriaBuilder.like(
                    criteriaBuilder.lower(root.get<String>(Constants.Column.ORDER_EMAIL_COLUMN)),
                    "%" + email.lowercase(Locale.getDefault()) + "%"
                )
            }
            criteriaBuilder.conjunction()
        }
    }

    private fun withOrderStatus(orderStatus: List<OrderStatus?>?): Specification<Order?> {
        return Specification { root: Root<Order?>, _: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (CollectionUtils.isNotEmpty(orderStatus)) {
                return@Specification root.get<Any>(Constants.Column.ORDER_ORDER_STATUS_COLUMN).`in`(orderStatus)
            }
            criteriaBuilder.conjunction()
        }
    }

    private fun withProductName(productName: String): Specification<Order?> {
        return Specification { root: Root<Order?>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (query == null || StringUtils.isEmpty(productName)) {
                return@Specification criteriaBuilder.conjunction()
            }
            val subquery = query.subquery(Long::class.java)
            val orderItemRoot = subquery.from(OrderItem::class.java)
            subquery.select(orderItemRoot.get(Constants.Column.ORDER_ORDER_ID_COLUMN))
            subquery.where(
                criteriaBuilder.and(
                    criteriaBuilder.equal(
                        orderItemRoot.get<Any>(Constants.Column.ORDER_ORDER_ID_COLUMN),
                        root.get<Any>(Constants.Column.ID_COLUMN)
                    ),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(orderItemRoot.get(Constants.Column.ORDER_ITEM_PRODUCT_NAME_COLUMN)),
                        "%" + productName.lowercase(Locale.getDefault()) + "%"
                    )
                )
            )
            criteriaBuilder.exists(subquery)
        }
    }

    private fun withBillingPhoneNumber(billingPhoneNumber: String?): Specification<Order?> {
        return Specification { root: Root<Order?>, _: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (!billingPhoneNumber.isNullOrEmpty()) {
                return@Specification criteriaBuilder.like(
                    criteriaBuilder.lower(
                        root.get<Any>(Constants.Column.ORDER_BILLING_ADDRESS_ID_COLUMN)
                            .get<String>(Constants.Column.ORDER_PHONE_COLUMN)
                    ),
                    "%" + billingPhoneNumber.lowercase(Locale.getDefault()) + "%"
                )
            }
            criteriaBuilder.conjunction()
        }
    }

    private fun withCountryName(countryName: String?): Specification<Order?> {
        return Specification { root: Root<Order?>, _: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (!countryName.isNullOrEmpty()) {
                return@Specification criteriaBuilder.like(
                    criteriaBuilder.lower(
                        root.get<Any>(Constants.Column.ORDER_BILLING_ADDRESS_ID_COLUMN)
                            .get<String>(Constants.Column.ORDER_COUNTRY_NAME_COLUMN)
                    ),
                    "%" + countryName.lowercase(Locale.getDefault()) + "%"
                )
            }
            criteriaBuilder.conjunction()
        }
    }

    private fun withDateRange(createdFrom: ZonedDateTime?, createdTo: ZonedDateTime?): Specification<Order?> {
        return Specification { root: Root<Order?>, _: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (createdFrom != null && createdTo != null) {
                return@Specification criteriaBuilder.between<ZonedDateTime>(
                    root.get<ZonedDateTime>(Constants.Column.CREATE_ON_COLUMN),
                    createdFrom,
                    createdTo
                )
            }
            criteriaBuilder.conjunction()
        }
    }
}

