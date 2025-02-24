package com.ecommerce.app.cart.model

import com.ecommerce.app.cart.listener.CustomAuditingEntityListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import java.time.ZonedDateTime

@MappedSuperclass
@EntityListeners(CustomAuditingEntityListener::class)
abstract class AbstractAuditEntity {

    @CreationTimestamp
    var createdOn: ZonedDateTime? = null

    @CreatedBy
    var createdBy: String? = null

    @UpdateTimestamp
    var lastModifiedOn: ZonedDateTime? = null

    @LastModifiedBy
    var lastModifiedBy: String? = null
}
