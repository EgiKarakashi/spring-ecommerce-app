package com.ecommerce.app.commonlibrary.exception

import com.ecommerce.app.commonlibrary.utils.MessageUtils

class StockExistingException(errorCode: String, vararg args: Any?) :
    RuntimeException(MessageUtils.getMessage(errorCode, *args)) {
    override val message: String = MessageUtils.getMessage(errorCode, *args)
}
