package com.ecommerce.app.commonlibrary.exception

import com.ecommerce.app.commonlibrary.utils.MessageUtils

class BadRequestException: RuntimeException {
    override val message: String

    constructor(message: String): super(MessageUtils.getMessage(message)) {
        this.message = MessageUtils.getMessage(message)
    }

    constructor(errorCode: String, vararg args: Any?): super(MessageUtils.getMessage(errorCode, *args)) {
        this.message = MessageUtils.getMessage(errorCode, *args)
    }
}
