package io.reeserve.dto

import io.reeserve.models.Customer
import java.io.Serializable

data class CustomerResponseDTO(
    val id: Long,
    val firstName: String,
    val lastName: String
) : Serializable {

    companion object {
        fun of(customer: Customer): CustomerResponseDTO {
            return CustomerResponseDTO(
                id = customer.id,
                firstName = customer.firstName,
                lastName = customer.lastName
            )
        }
    }

}
