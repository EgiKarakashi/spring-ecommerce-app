package com.ecommerce.app.media.exception

import com.ecommerce.app.commonlibrary.exception.NotFoundException
import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.ControllerAdvice
import com.ecommerce.app.commonlibrary.exception.UnsupportedMediaTypeException
import com.ecommerce.app.media.viewmodel.ErrorVm
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
@Slf4j
class ControllerAdvisor {
    private val ERROR_LOG_FORMAT: String = "Error: URI: {}, ErrorCode: {}, Message: {}"

    @ExceptionHandler(UnsupportedMediaTypeException::class)
    fun handleUnsupportedMediaTypeException(ex: UnsupportedMediaTypeException, request: WebRequest): ResponseEntity<ErrorVm> {
        val status = HttpStatus.BAD_REQUEST
        val message = ex.message

        return buildErrorResponse(status, message, null, ex, request, 404, "")
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException, request: WebRequest): ResponseEntity<ErrorVm> {
        val status = HttpStatus.NOT_FOUND
        val message = ex.message
        return buildErrorResponse(status, message, null, ex, request, 404,"")
    }


    @ExceptionHandler(Exception::class)
    fun handleOtherException(ex: Exception, request: WebRequest): ResponseEntity<ErrorVm> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val message = ex.message
        return buildErrorResponse(status, message, null, ex, request, 500, "")
    }

    private fun getServletPath(webRequest: WebRequest): String {
        val servletRequest = webRequest as ServletWebRequest
        return servletRequest.request.servletPath
    }

    private fun buildErrorResponse(status: HttpStatus, message: String?, errors: List<String>?, ex: Exception,
                                   request: WebRequest, statusCode: Int, title: String): ResponseEntity<ErrorVm> {
        val errorVm = ErrorVm(status.toString(), if (title.isEmpty()) status.reasonPhrase else title, message, errors)
        request?.let {
            log.error(ERROR_LOG_FORMAT, getServletPath(it), statusCode, message)
        }
        log.error(message, ex)
        return ResponseEntity.status(status).body(errorVm)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ControllerAdvisor::class.java)
    }
}
