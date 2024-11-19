package com.ecommerce.app.commonlibrary.exception

import com.ecommerce.app.commonlibrary.utils.MessageUtils

class Forbidden(errorCode: String, vararg var2: Any) : RuntimeException() {
    override var message: String = MessageUtils.getMessage(errorCode, var2)
        private set

//     fun getMessage(): String {
//        return message
//    }
}
