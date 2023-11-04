package io.reeserve.models

import io.reeserve.dto.CustomerRequestDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "CUSTOMERS")
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(name = "first_name")
    val firstName: String,
    @Column(name = "last_name")
    val lastName: String
) {
    companion object {
        fun of(customerRequestDTO: CustomerRequestDTO): Customer {
            return Customer(
                firstName = customerRequestDTO.firstName,
                lastName = customerRequestDTO.lastName
            )
        }
    }
}
