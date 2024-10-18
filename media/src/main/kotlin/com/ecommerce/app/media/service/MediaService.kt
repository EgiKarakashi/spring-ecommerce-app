package com.ecommerce.app.media.service

import com.ecommerce.app.media.model.Media
import com.ecommerce.app.media.model.dto.MediaDto
import com.ecommerce.app.media.viewmodel.MediaPostVm
import com.ecommerce.app.media.viewmodel.MediaVm

interface MediaService {
    fun saveMedia(mediaPostVm: MediaPostVm): Media
    fun getMediaById(id: Long): MediaVm?
    fun removeMedia(id: Long): Unit
    fun getFile(id: Long, fileName: String): MediaDto?
}
