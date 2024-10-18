package com.ecommerce.app.media.repository

import com.ecommerce.app.media.model.Media
import com.ecommerce.app.media.viewmodel.NoFileMediaVm
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MediaRepository: JpaRepository<Media, Long> {

    @Query("SELECT NEW com.ecommerce.app.media.viewmodel.NoFileMediaVm(m.id, m.caption, m.fileName, m.mediaType) FROM Media m WHERE m.id = ?1")
    fun findByIdWithoutFileInReturn(id: Long): NoFileMediaVm?
}
