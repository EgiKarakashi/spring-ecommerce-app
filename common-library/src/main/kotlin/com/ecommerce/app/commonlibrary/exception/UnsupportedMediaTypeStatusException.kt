package com.ecommerce.app.commonlibrary.exception

class UnsupportedMediaTypeException : RuntimeException {
    constructor() : super()

    constructor(errorMessage: String) : super(errorMessage)

    constructor(cause: Throwable) : super(cause)

    constructor(errorMessage: String, err: Throwable) : super(errorMessage, err)
}

