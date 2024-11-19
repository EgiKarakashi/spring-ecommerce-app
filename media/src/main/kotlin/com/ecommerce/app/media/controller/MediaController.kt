package com.ecommerce.app.media.controller

import com.ecommerce.app.media.service.MediaService
import com.ecommerce.app.media.viewmodel.ErrorVm
import com.ecommerce.app.media.viewmodel.MediaPostVm
import com.ecommerce.app.media.viewmodel.MediaVm
import com.ecommerce.app.media.viewmodel.NoFileMediaVm
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Paths

@Validated
@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
class MediaController(
    val mediaService: MediaService,
    val resourceLoader: ResourceLoader
) {

    @PostMapping(path = ["/medias"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ok",
                content = [Content(schema = Schema(implementation = NoFileMediaVm::class))]),
            ApiResponse(responseCode = "400", description = "Bad request",
                content = [Content(schema = Schema(implementation = ErrorVm::class))])
        ]
    )
    fun create(@ModelAttribute @Valid mediaPostVm: MediaPostVm): ResponseEntity<Any> {
        val media = mediaService.saveMedia(mediaPostVm)
        val noFileMediaVm = NoFileMediaVm(media.id, media.caption, media.fileName, media.mediaType)
        return ResponseEntity.ok().body(noFileMediaVm)
    }


    @DeleteMapping("/medias/{id}")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Deleted",
                content = [Content(schema = Schema(implementation =  MediaVm::class))]),
            ApiResponse(responseCode = "400", description = "Bad request",
                content = [Content(schema = Schema(implementation = ErrorVm::class))])
        ]
    )
    fun delete(@PathVariable id: Long):ResponseEntity<Unit> {
        mediaService.removeMedia(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/medias/{id}")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ok",
                content = [Content(schema = Schema(implementation = MediaVm::class))])
        ]
    )
    fun get(@PathVariable id: Long): ResponseEntity<MediaVm> {
        val media = mediaService.getMediaById(id)
        if (media == null) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok().body(media)
    }

    @Hidden
    @GetMapping("/medias/{id}/file/{fileName}")
    fun getFile(@PathVariable id: Long, @PathVariable fileName: String): ResponseEntity<InputStreamResource> {
        val mediaDto = mediaService.getFile(id, fileName)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
            .contentType(mediaDto?.mediaType!!)
            .body(InputStreamResource(mediaDto.content!!))
    }
}
