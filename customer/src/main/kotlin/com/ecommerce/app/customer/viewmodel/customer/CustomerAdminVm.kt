package com.ecommerce.app.customer.viewmodel.customer

import org.keycloak.representations.idm.UserRepresentation
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone

data class CustomerAdminVm(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdTimestamp: LocalDateTime
) {
    companion object {
        fun fromUserRepresentation(userRepresentation: UserRepresentation): CustomerAdminVm {
            val createdTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(userRepresentation.createdTimestamp), TimeZone.getDefault().toZoneId())

            return CustomerAdminVm(
                userRepresentation.id,
                userRepresentation.username,
                userRepresentation.email,
                userRepresentation.firstName,
                userRepresentation.lastName,
                createdTimestamp
            )
        }
    }
}
