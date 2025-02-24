package com.ecommerce.app.springecommerceapp.vector.common.document

import org.springframework.ai.document.id.IdGenerator
import java.util.UUID

class DefaultIdGenerator(
    val identity: Long,
    val idPrefix: String
): IdGenerator {
    override fun generateId(vararg contents: Any?): String {
        val id = "%s-%s".formatted(idPrefix, identity)
        return UUID.nameUUIDFromBytes(id.toByteArray()).toString()
    }
}
