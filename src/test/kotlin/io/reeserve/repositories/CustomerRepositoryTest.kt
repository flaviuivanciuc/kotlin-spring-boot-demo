package io.reeserve.repositories

import io.reeserve.models.Customer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest @Autowired constructor(
    val customerRepository: CustomerRepository
) {

    @Test
    fun `should save and fetch customer`() {
        // Given
        val customerToSave = Customer(firstName = "John", lastName = "Cena")

        // When
        val savedCustomer = customerRepository.save(customerToSave)
        val retrievedCustomer = customerRepository.findById(savedCustomer.id).orElseThrow()

        // Then
        assertThat(retrievedCustomer).isNotNull
        assertThat(retrievedCustomer.id).isEqualTo(savedCustomer.id)
    }

    @Test
    fun `should delete a customer`() {
        // Given
        val customerToSave = Customer(firstName = "John", lastName = "Cena")
        val savedCustomer = customerRepository.save(customerToSave)

        // When
        customerRepository.delete(savedCustomer)

        // Then
        val retrievedCustomer = customerRepository.findById(savedCustomer.id).orElse(null)
        assertThat(retrievedCustomer).isNull()
    }
}