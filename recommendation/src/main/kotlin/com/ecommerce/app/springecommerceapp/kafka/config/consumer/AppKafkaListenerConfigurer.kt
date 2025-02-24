package com.ecommerce.app.springecommerceapp.kafka.config.consumer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListenerConfigurer
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@EnableKafka
@Configuration
class AppKafkaListenerConfigurer: KafkaListenerConfigurer {

    @Autowired
    private lateinit var validator: LocalValidatorFactoryBean

    override fun configureKafkaListeners(registrar: KafkaListenerEndpointRegistrar) {
        registrar.setValidator(validator)
    }
}
