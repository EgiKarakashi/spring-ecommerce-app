package com.ecommerce.app.customer.viewmodel.customer

import org.keycloak.representations.idm.UserRepresentation

data class CustomerVm(
    val id: String?,
    val username: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?
) {
    companion object {
        fun fromUserRepresentation(userRepresentation: UserRepresentation): CustomerVm {
            return CustomerVm(
                userRepresentation.id,
                userRepresentation.username,
                userRepresentation.email,
                userRepresentation.firstName,
                userRepresentation.lastName
            )
        }
    }
}
