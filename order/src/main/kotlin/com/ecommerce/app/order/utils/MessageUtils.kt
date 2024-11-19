package com.ecommerce.app.order.utils

import org.slf4j.helpers.MessageFormatter
import java.util.*

object MessagesUtils {

    private val messageBundle: ResourceBundle = ResourceBundle.getBundle("messages.messages", Locale.getDefault())

    fun getMessage(errorCode: String, vararg args: Any?): String {
        val message = try {
            messageBundle.getString(errorCode)
        } catch (ex: MissingResourceException) {
            // If message code is not defined, return the error code itself
            errorCode
        }
        return MessageFormatter.arrayFormat(message, args).message
    }
}
