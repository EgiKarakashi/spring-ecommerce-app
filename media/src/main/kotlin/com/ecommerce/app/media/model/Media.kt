package com.ecommerce.app.media.model

import jakarta.persistence.*

@Entity
@Table(name = "media")
data class Media(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var caption: String? = null,
    var fileName: String? = null,
    var filePath: String? = null,
    var mediaType: String? = null
)
