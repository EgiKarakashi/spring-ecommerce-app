package com.ecommerce.app.rating.model

import com.ecommerce.app.rating.listener.CustomAuditingEntityListener
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import java.time.ZonedDateTime

@MappedSuperclass
@EntityListeners(CustomAuditingEntityListener::class)
abstract class AbstractAuditEntity(
    @CreationTimestamp
    val createdOn: ZonedDateTime? = null,

    @CreatedBy
    var createdBy: String? = null,

    @UpdateTimestamp
    val lastModifiedOn: ZonedDateTime? = null,

    @LastModifiedBy
    var lastModifiedBy: String? = null
)
