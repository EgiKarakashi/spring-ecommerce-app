package com.ecommerce.app.commonlibrary.exception

import com.ecommerce.app.commonlibrary.utils.MessageUtils

class ResourceExistedException(
    errorCode: String,
    vararg args: Any
) : RuntimeException(MessageUtils.getMessage(errorCode, args)) {

    override var message: String = MessageUtils.getMessage(errorCode, args)
}


