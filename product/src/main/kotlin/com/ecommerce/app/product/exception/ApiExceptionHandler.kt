package com.ecommerce.app.product.exception

import com.ecommerce.app.commonlibrary.exception.BadRequestException
import com.ecommerce.app.commonlibrary.exception.DuplicatedException
import com.ecommerce.app.commonlibrary.exception.InternalServerErrorException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.product.viewmodel.error.ErrorVm
import jakarta.validation.ConstraintViolationException
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.core.NestedExceptionUtils
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.dao.DataIntegrityViolationException

@ControllerAdvice
@Slf4j
class ApiExceptionHandler {
    companion object {
        private const val ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}"
        private val log = LoggerFactory.getLogger(ApiExceptionHandler::class.java)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException, request: WebRequest): ResponseEntity<ErrorVm> {
        val status = HttpStatus.NOT_FOUND
        val message = ex.message
        return buildErrorResponse(status, message, null, ex, request, 404)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException, request: WebRequest): ResponseEntity<ErrorVm> {
        return handleBadRequest(ex, false, request)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<ErrorVm> {
        val status = HttpStatus.BAD_REQUEST
        val errors = ex.bindingResult
            .fieldErrors
            .stream()
            .map { error -> error.field + " " + error.defaultMessage }
            .toList()
        return buildErrorResponse(status, "Request information is not valid", errors, ex, null, 0)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<ErrorVm> {
        val status = HttpStatus.BAD_REQUEST
        val errors = ex.constraintViolations.stream()
            .map { violation -> String.format("%s %s: %s ",
                violation.rootBeanClass.name,
                violation.propertyPath,
                violation.message) }
            .toList()

        return buildErrorResponse(status, "Request is not valid", errors, ex, null, 0)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<ErrorVm> {
        return handleBadRequest(ex, true, null)
    }

    @ExceptionHandler(DuplicatedException::class)
    fun handleDuplicated(ex: DuplicatedException): ResponseEntity<ErrorVm> {
        return handleBadRequest(ex, false, null)
    }

    @ExceptionHandler(InternalServerErrorException::class)
    fun handleInternalServerErrorException(ex: InternalServerErrorException): ResponseEntity<ErrorVm> {
        log.error("Internal server error exception: ", ex)
        val errorVm = ErrorVm(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase, ex.message)
        return ResponseEntity.internalServerError().body(errorVm)
    }

    @ExceptionHandler(Exception::class)
    fun handleOtherException(ex: Exception, request: WebRequest): ResponseEntity<ErrorVm> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val message = ex.message
        return buildErrorResponse(status, message, null, ex, request, 500)
    }

    private fun getServletPath(webRequest: WebRequest): String {
        val servletRequest = webRequest as ServletWebRequest
        return servletRequest.request.servletPath
    }

    private fun handleBadRequest(ex: Exception, isUsingNestedException: Boolean, request: WebRequest?): ResponseEntity<ErrorVm> {
        val status = HttpStatus.BAD_REQUEST
        val message = if (isUsingNestedException) {
            NestedExceptionUtils.getMostSpecificCause(ex).message
        } else {
            ex.message
        }
        return buildErrorResponse(status, message, null, ex, request, 400)
    }

    private fun buildErrorResponse(
        status: HttpStatus,
        message: String?,
        errors: List<String>?,
        ex: Exception,
        request: WebRequest?,
        statusCode: Int
    ): ResponseEntity<ErrorVm> {
        val safeMessage = message ?: "An unexpected error occurred"
        val safeErrors = errors ?: listOf("No error details available")

        val errorVm = ErrorVm(status.toString(), status.reasonPhrase, safeMessage, safeErrors)
        request?.let {
            log.error(ERROR_LOG_FORMAT, getServletPath(it), statusCode, safeMessage)
        }
        log.error(safeMessage, ex)

        return ResponseEntity.status(status).body(errorVm)
    }

}
