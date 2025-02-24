package com.ecommerce.app.springecommerceapp.vector.common.store

import com.ecommerce.app.springecommerceapp.vector.common.document.BaseDocument

interface VectorRepository<D: BaseDocument, E> {
    fun search(id: Long): List<D>
    fun getEntity(entityId: Long): E?
    fun add(entityId: Long)
    fun delete(entityId: Long)
    fun update(entityId: Long)
}
