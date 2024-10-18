package com.ecommerce.app.product.utils

import org.slf4j.helpers.FormattingTuple
import org.slf4j.helpers.MessageFormatter
import java.util.*

object MessagesUtils {

    private val messageBundle: ResourceBundle = ResourceBundle.getBundle("messages.messages", Locale.getDefault())

    fun getMessage(errorCode: String, vararg var2: Any): String {
        val message: String = try {
            messageBundle.getString(errorCode)
        } catch (ex: MissingResourceException) {
            // case message_code is not defined.
            errorCode
        }
        val formattingTuple: FormattingTuple = MessageFormatter.arrayFormat(message, var2)
        return formattingTuple.message
    }
}
