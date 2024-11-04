package com.ecommerce.app.search.kafka.config.consumer

import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListenerConfigurer
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@EnableKafka
@Configuration
class AppKafkaListenerConfigurer(
    private val validator: LocalValidatorFactoryBean
): KafkaListenerConfigurer {

    override fun configureKafkaListeners(registrar: KafkaListenerEndpointRegistrar) {
        registrar.setValidator(this.validator)
    }
}
