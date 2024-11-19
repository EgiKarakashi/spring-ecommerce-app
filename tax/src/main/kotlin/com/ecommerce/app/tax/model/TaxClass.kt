package com.ecommerce.app.tax.model

import jakarta.persistence.*

@Entity
@Table(name = "tax_class")
data class TaxClass(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 450)
    val name: String? = null
): AbstractAuditEntity()
