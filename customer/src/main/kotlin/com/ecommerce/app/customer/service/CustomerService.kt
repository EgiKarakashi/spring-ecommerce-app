package com.ecommerce.app.customer.service

import com.ecommerce.app.commonlibrary.exception.AccessDeniedException
import com.ecommerce.app.commonlibrary.exception.NotFoundException
import com.ecommerce.app.commonlibrary.exception.WrongEmailFormatException
import com.ecommerce.app.customer.config.KeycloakPropsConfig
import com.ecommerce.app.customer.utils.Constants
import com.ecommerce.app.customer.viewmodel.customer.CustomerAdminVm
import com.ecommerce.app.customer.viewmodel.customer.CustomerListVm
import org.apache.commons.validator.routines.EmailValidator
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.springframework.stereotype.Service
import javax.ws.rs.ForbiddenException

@Service
class CustomerService(
    private val ERROR_FORMAT: String = "%s: Client %s doesn't have access rights for this resource",
    private val USER_PER_PAGE: Int = 2,
    private val keycloak: Keycloak,
    private val keycloakPropsConfig: KeycloakPropsConfig
) {
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
}

