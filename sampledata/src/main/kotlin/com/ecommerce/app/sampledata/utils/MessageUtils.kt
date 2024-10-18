package com.ecommerce.app.sampledata.utils

import org.slf4j.helpers.FormattingTuple
import org.slf4j.helpers.MessageFormatter
import java.util.*

object MessageUtils {
        private val messageBundle: ResourceBundle = ResourceBundle.getBundle("messages.messages", Locale.getDefault())

        fun getMessage(errorCode: String, vararg args: Any?): String {
            val message = try {
                messageBundle.getString(errorCode)
            } catch (ex: MissingResourceException) {
                errorCode
            }
            val formattingTuple: FormattingTuple = MessageFormatter.arrayFormat(message, args)
            return formattingTuple.message
        }
}
