package io.reeserve.services

import io.reeserve.dto.CustomerRequestDTO
import io.reeserve.dto.CustomerResponseDTO
import io.reeserve.exceptions.ResourceNotFoundException
import io.reeserve.models.Customer
import io.reeserve.repositories.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) {

    fun getAllCustomers(pageable: Pageable): Page<CustomerResponseDTO> {
        return customerRepository.findAll(pageable).map { CustomerResponseDTO.of(it) }
    }

    @Transactional
    fun addCustomer(customerRequest: CustomerRequestDTO): CustomerResponseDTO {
        val customer = Customer.of(customerRequest)
        return CustomerResponseDTO.of(customerRepository.save(customer))
    }

    fun deleteCustomer(id: Long) {
        val customer =
            customerRepository.findById(id).orElseThrow { ResourceNotFoundException("Customer with ID $id not found") }
        customerRepository.delete(customer)
    }
}
