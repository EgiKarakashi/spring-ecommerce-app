package com.ecommerce.app.customer.service

import com.ecommerce.app.commonlibrary.exception.AccessDeniedException
import com.ecommerce.app.commonlibrary.exception.DuplicatedException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.commonlibrary.exception.WrongEmailFormatException
import com.ecommerce.app.customer.config.KeycloakPropsConfig
import com.ecommerce.app.customer.utils.Constants
import com.ecommerce.app.customer.viewmodel.customer.CustomerAdminVm
import com.ecommerce.app.customer.viewmodel.customer.CustomerListVm
import com.ecommerce.app.customer.viewmodel.customer.CustomerPostVm
import com.ecommerce.app.customer.viewmodel.customer.CustomerVm
import org.apache.commons.validator.routines.EmailValidator
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service
import javax.ws.rs.ForbiddenException

@Service
class CustomerService(
    private val keycloak: Keycloak,
    private val keycloakPropsConfig: KeycloakPropsConfig
) {
    private val ERROR_FORMAT: String = "%s: Client %s doesn't have access rights for this resource"
    private val USER_PER_PAGE: Int = 2
    companion object {
        fun createPasswordCredentials(password: String): CredentialRepresentation {
            return CredentialRepresentation().apply {
                isTemporary = false
                type = CredentialRepresentation.PASSWORD
                value = password
            }
        }
    }

    fun getCustomers(pageNo: Int): CustomerListVm {
        return try {
            val result = keycloak.realm(keycloakPropsConfig.realm).users()
                .search(null, pageNo * USER_PER_PAGE, USER_PER_PAGE)
                .map { CustomerAdminVm.fromUserRepresentation(it) }

            val totalUser = keycloak.realm(keycloakPropsConfig.realm).users().count()

            CustomerListVm(totalUser, result, (totalUser + USER_PER_PAGE - 1) / USER_PER_PAGE)
        } catch (exception: ForbiddenException) {
            throw AccessDeniedException(
                String.format(ERROR_FORMAT, exception.message, keycloakPropsConfig.resource)
            )
        }
    }

    fun getCustomerByEmail(email: String): CustomerAdminVm {
        try {
            if (EmailValidator.getInstance().isValid(email)) {
                val searchResult = keycloak.realm(keycloakPropsConfig.realm).users().search(email, true)
                if (searchResult.isEmpty()) {
                    throw NotFoundException(Constants.ErrorCode.USER_WITH_EMAIL_NOT_FOUND, email)
                }
                return CustomerAdminVm.fromUserRepresentation(searchResult[0])
            } else {
                throw WrongEmailFormatException(Constants.ErrorCode.WRONG_EMAIL_FORMAT, email)
            }
        } catch (ex: ForbiddenException) {
            throw AccessDeniedException(String.format(ERROR_FORMAT, ex.message, keycloakPropsConfig.resource))
        }
    }

    fun create(customerPostVm: CustomerPostVm): CustomerVm {
        // Get realm
        val realmResource = keycloak.realm(keycloakPropsConfig.realm)

        if (checkUsernameExists(realmResource, customerPostVm.username)) {
            throw DuplicatedException(Constants.ErrorCode.USERNAME_ALREADY_EXITED, customerPostVm.username)
        }
        if (checkEmailExists(realmResource, customerPostVm.email)) {
            throw DuplicatedException(Constants.ErrorCode.USER_WITH_EMAIL_ALREADY_EXITED, customerPostVm.email)
        }

        // Define user
        val user = UserRepresentation().apply {
            username = customerPostVm.username
            firstName = customerPostVm.firstName
            lastName = customerPostVm.lastName
            email = customerPostVm.email
            credentials = listOf(createPasswordCredentials(customerPostVm.password))
            isEnabled = true
        }

        val response = realmResource.users().create(user)

        // Get new user
        val userId = CreatedResponseUtil.getCreatedId(response)
        val userResource = realmResource.users().get(userId)

        // Assign realm role to user
        val realmRole = realmResource.roles().get(customerPostVm.role).toRepresentation()
        userResource.roles().realmLevel().add(listOf(realmRole))

        return CustomerVm.fromUserRepresentation(user)
    }

    fun checkUsernameExists(realmResource: RealmResource, username: String): Boolean {
        val users = realmResource.users().search(username, true)
        return !users.isEmpty()
    }


    fun checkEmailExists(realmResource: RealmResource, email: String): Boolean {
        val users = realmResource.users().search(null, null, null, email, 0, 1)
        return !users.isEmpty()
    }

    fun getCustomerProfile(userId: String): CustomerVm {
        try {
            return CustomerVm.fromUserRepresentation(keycloak.realm(keycloakPropsConfig.realm).users().get(userId).toRepresentation())
        } catch (ex: ForbiddenException) {
            throw AccessDeniedException(String.format(ERROR_FORMAT, ex.message, keycloakPropsConfig.resource))
        }
    }
}

