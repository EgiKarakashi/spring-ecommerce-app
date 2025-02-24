package com.ecommerce.app.cart.listener

import com.ecommerce.app.cart.model.AbstractAuditEntity
import jakarta.persistence.PrePersist
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.data.auditing.AuditingHandler
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Configurable
class CustomAuditingEntityListener(handler: ObjectFactory<AuditingHandler>): AuditingEntityListener() {
    init {
        setAuditingHandler(handler)
    }

    @PrePersist
    override fun touchForCreate(target: Any) {
        val entity = target  as AbstractAuditEntity
        if (entity.createdBy == null) {
            super.touchForCreate(target)
        } else {
            if (entity.lastModifiedBy == null) {
                entity.lastModifiedBy = entity.createdBy
            }
        }
    }

    override fun touchForUpdate(target: Any) {
        val entity = target as AbstractAuditEntity
        if (entity.lastModifiedBy == null) {
            super.touchForUpdate(target)
        }
    }
}
