package com.ecommerce.app.commonlibrary.utils

import org.slf4j.helpers.FormattingTuple
import org.slf4j.helpers.MessageFormatter
import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle

object MessageUtils {
    private val messageBundle: ResourceBundle = ResourceBundle.getBundle("messages.messages", Locale.getDefault())

    fun getMessage(errorCode: String, vararg  args: Any?): String {
        val message: String = try {
            messageBundle.getString(errorCode)
        } catch (ex: MissingResourceException) {
            errorCode
        }
        val formattingTuple: FormattingTuple = MessageFormatter.arrayFormat(message, args)
        return formattingTuple.message
    }
}
