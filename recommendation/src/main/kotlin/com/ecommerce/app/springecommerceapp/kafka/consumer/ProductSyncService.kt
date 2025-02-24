package com.ecommerce.app.springecommerceapp.kafka.consumer

import com.ecommerce.app.commonlibrary.kafka.cdc.message.Operation
import com.ecommerce.app.commonlibrary.kafka.cdc.message.ProductCdcMessage
import com.ecommerce.app.commonlibrary.kafka.cdc.message.ProductMsgKey
import com.ecommerce.app.springecommerceapp.vector.product.service.ProductVectorSyncService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductSyncService {

    @Autowired
    private lateinit var productVectorSyncService: ProductVectorSyncService

    fun sync(key: ProductMsgKey, productCdcMessage: ProductCdcMessage?) {
        val isHardDeleteEvent = productCdcMessage == null || Operation.DELETE.equals(productCdcMessage.op)
        if (isHardDeleteEvent) {
            log.warn("Having hard delete event for product: ${key.id}")
            productVectorSyncService.deleteProductVector(key.id)
        } else if (productCdcMessage?.after != null) {
            val operation = productCdcMessage.op
            val product = productCdcMessage.after
            when (operation) {
                Operation.CREATE, Operation.READ -> productVectorSyncService.createProductVector(product)
                Operation.UPDATE -> productVectorSyncService.updateProductVector(product)
                else -> log.warn("Unsupported operation: $operation for product: ${product.id}")
            }
        }

    }

    companion object {
        private val log = LoggerFactory.getLogger(ProductSyncService::class.java)
    }
}
