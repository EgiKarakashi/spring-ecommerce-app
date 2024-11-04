package com.ecommerce.app.media.service

import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.media.config.EcommerceConfig
import com.ecommerce.app.media.model.Media
import com.ecommerce.app.media.model.dto.MediaDto
import com.ecommerce.app.media.repository.FileSystemRepository
import com.ecommerce.app.media.repository.MediaRepository
import com.ecommerce.app.media.utils.StringUtils
import com.ecommerce.app.media.viewmodel.MediaPostVm
import com.ecommerce.app.media.viewmodel.MediaVm
import com.ecommerce.app.media.viewmodel.NoFileMediaVm
import lombok.SneakyThrows
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class MediaServiceImpl(
    private val mediaRepository: MediaRepository,
    private val fileSystemRepository: FileSystemRepository,
    private val ecommerceConfig: EcommerceConfig
): MediaService {

    @SneakyThrows
    override fun saveMedia(mediaPostVm: MediaPostVm): Media {
        val media = Media()
        media.caption = mediaPostVm.caption
        media.mediaType = mediaPostVm.multipartFile?.contentType

        if (StringUtils.hasText(mediaPostVm.fileNameOverride)) {
            media.fileName = mediaPostVm.multipartFile.toString().trim()
        } else {
            media.fileName = mediaPostVm.multipartFile?.originalFilename
        }
        val filePath = fileSystemRepository.persistFile(media.fileName!!, mediaPostVm.multipartFile!!.bytes)
        media.filePath = filePath
        return mediaRepository.save(media)
    }

    override fun removeMedia(id: Long) {
        val noFileMediaVm = mediaRepository.findByIdWithoutFileInReturn(id)
        if (noFileMediaVm == null) {
            throw NotFoundException(String.format("Media $id is not found"))
        }
        mediaRepository.deleteById(id)
    }

    override fun getMediaById(id: Long): MediaVm? {
       val noFileMediaVm: NoFileMediaVm? = mediaRepository.findByIdWithoutFileInReturn(id)
        if (noFileMediaVm == null) {
            return null
        }
        val url = UriComponentsBuilder.fromUriString(ecommerceConfig.publicUrl)
            .path(String.format("/medias/%1\$s/file/%2\$s", noFileMediaVm.id, noFileMediaVm.fileName))
            .build()
            .toUriString()
        return MediaVm(
            noFileMediaVm.id,
            noFileMediaVm.caption,
            noFileMediaVm.fileName,
            noFileMediaVm.mediaType,
            url
        )
    }

    override fun getFile(id: Long, fileName: String): MediaDto? {
        val media = mediaRepository.findById(id).orElse(null)

        if (!fileName.equals(media.fileName, ignoreCase = true)) {
            return null
        }

        val mediaType = MediaType.valueOf(media.mediaType!!)
        val fileContent = fileSystemRepository.getFile(media.filePath!!)

        return MediaDto(
            content = fileContent,
            mediaType = mediaType
        )
    }
}
