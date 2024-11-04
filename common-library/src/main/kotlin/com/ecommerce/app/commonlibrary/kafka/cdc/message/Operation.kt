package com.ecommerce.app.commonlibrary.kafka.cdc.message

enum class Operation( value: String) {
    READ("r"),
    CREATE("c"),
    UPDATE("u"),
    DELETE("D")
}
