package com.ecommerce.app.rating.model

import jakarta.persistence.*

@Entity
@Table(name = "rating")
data class Rating(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
      val id: Long? = null,
    var content: String? = null,
    var ratingStar: String? = null,
    var productId: Long? = null,
    var productName: String? = null,
    var lastName: String? = null,
    var firstName: String? = null
): AbstractAuditEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rating) return false
        if (id != null && id == other.id) return true
        return false
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
