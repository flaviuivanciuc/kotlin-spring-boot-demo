package io.reeserve.services

import io.reeserve.dto.CustomerRequestDTO
import io.reeserve.models.Customer
import io.reeserve.repositories.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class CustomerServiceTest {

    @Mock
    lateinit var customerRepository: CustomerRepository

    @InjectMocks
    lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        // Given
        val pageable = PageRequest.of(0, 5)
        val customer = Customer(firstName = "John", lastName = "Cena")
        val customerPage = PageImpl(listOf(customer))

        // Define behaviour
        `when`(customerRepository.findAll(pageable)).thenReturn(customerPage)

        // When
        val result = customerService.getAllCustomers(pageable)

        // Then
        assertThat(result).isNotNull()
        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].firstName).isEqualTo("John")
        assertThat(result.content[0].lastName).isEqualTo("Cena")

        // Verify interactions with mock
        verify(customerRepository).findAll(pageable)
    }

    @Test
    fun `should add customer`() {
        // Given
        val customerRequestDTO = CustomerRequestDTO(firstName = "John", lastName = "Cena")
        val customer = Customer.of(customerRequestDTO)

        // Define behaviour
        `when`(customerRepository.save(any(Customer::class.java))).thenReturn(customer)

        // When
        val result = customerService.addCustomer(customerRequestDTO)

        // Then
        assertThat(result).isNotNull()
        assertThat(result.firstName).isEqualTo("John")
        assertThat(result.lastName).isEqualTo("Cena")

        // Verify interactions with mock
        verify(customerRepository).save(any(Customer::class.java))
    }

    @Test
    fun `should delete customer`() {
        // Given
        val customerId = 1L
        val customer = Customer(firstName = "John", lastName = "Doe")

        // Define behaviour
        `when`(customerRepository.findById(customerId)).thenReturn(Optional.of(customer))

        // When
        customerService.deleteCustomer(customerId)

        // Then
        // We might just want to ensure method execution finishes without exception

        // Verify interactions with mock
        verify(customerRepository).findById(customerId)
        verify(customerRepository).delete(customer)
    }
}