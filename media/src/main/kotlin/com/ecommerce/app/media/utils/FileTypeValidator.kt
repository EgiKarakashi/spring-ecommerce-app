package com.ecommerce.app.media.utils

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

class FileTypeValidator : ConstraintValidator<ValidFileType, MultipartFile> {

    private lateinit var allowedTypes: Array<String>
    private lateinit var message: String

    override fun initialize(constraintAnnotation: ValidFileType) {
        allowedTypes = constraintAnnotation.allowedTypes
        message = constraintAnnotation.message
    }

    override fun isValid(file: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        if (file == null || file.contentType == null) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
            return false
        }

        allowedTypes.forEach { type ->
            if (type == file.contentType) {
                return try {
                    val image: BufferedImage? = ImageIO.read(file.inputStream)
                    image != null
                } catch (e: IOException) {
                    false
                }
            }
        }

        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
        return false
    }
}

