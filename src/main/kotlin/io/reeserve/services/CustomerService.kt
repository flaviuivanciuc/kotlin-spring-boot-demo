package io.reeserve.services

import io.reeserve.dto.CustomerRequestDTO
import io.reeserve.dto.CustomerResponseDTO
import io.reeserve.exceptions.ResourceNotFoundException
import io.reeserve.models.Customer
import io.reeserve.repositories.CustomerRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) {

    @Cacheable(cacheNames = ["Customer"], key = "#id")
    fun getCustomer(id: Long): CustomerResponseDTO {
        return CustomerResponseDTO.of(customerRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Customer with ID $id not found") })
    }

    fun getAllCustomers(pageable: Pageable): Page<CustomerResponseDTO> {
        return customerRepository.findAll(pageable).map { CustomerResponseDTO.of(it) }
    }

    @Transactional
    @CachePut(cacheNames = ["Customer"], key = "#result.id")
    fun addCustomer(customerRequest: CustomerRequestDTO): CustomerResponseDTO {
        val customer = Customer.of(customerRequest)
        return CustomerResponseDTO.of(customerRepository.save(customer))
    }

    @CacheEvict(cacheNames = ["Customer"], key = "#id")
    fun deleteCustomer(id: Long) {
        val customer =
            customerRepository.findById(id).orElseThrow { ResourceNotFoundException("Customer with ID $id not found") }
        customerRepository.delete(customer)
    }
}
