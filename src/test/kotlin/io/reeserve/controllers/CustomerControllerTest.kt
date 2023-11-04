package io.reeserve.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import io.reeserve.config.TestSecurityConfig
import io.reeserve.dto.CustomerRequestDTO
import io.reeserve.dto.CustomerResponseDTO
import io.reeserve.exceptions.ResourceNotFoundException
import io.reeserve.services.CustomerService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(MockitoExtension::class)
@WebMvcTest(controllers = [CustomerController::class])
@Import(TestSecurityConfig::class)
class CustomerControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        // Given
        val pageable = PageRequest.of(0, 5)
        val customerResponseDTO = CustomerResponseDTO(id = 1L, firstName = "John", lastName = "Doe")
        val customerPage = PageImpl(listOf(customerResponseDTO))

        // Define behaviour
        `when`(customerService.getAllCustomers(pageable)).thenReturn(customerPage)

        // Perform and validate request
        mockMvc.perform(
            get("/customers")
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(1L))
            .andExpect(jsonPath("$.content[0].firstName").value("John"))
            .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
    }

    @Test
    fun `should add customer and return customer response`() {
        // Given
        val customerRequestDTO = CustomerRequestDTO(firstName = "John", lastName = "Doe")
        val customerResponseDTO = CustomerResponseDTO(id = 1L, firstName = "John", lastName = "Doe")

        // Define behavior
        `when`(customerService.addCustomer(customerRequestDTO))
            .thenReturn(customerResponseDTO)

        // Perform and validate request
        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
    }

    @Test
    fun `should delete customer and return no content`() {
        // Given
        val customerId = 1L

        // No behavior definition needed, as method has no return value

        // Perform and validate request
        mockMvc.perform(delete("/customers/$customerId").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should return 404 when deleting non-existing customer`() {
        // Given
        val nonExistentCustomerId = 100L

        // Define behavior: when service tries to delete customer 100, throw ResourceNotFoundException
        `when`(customerService.deleteCustomer(nonExistentCustomerId))
            .thenThrow(ResourceNotFoundException("Customer with ID $nonExistentCustomerId not found"))

        // Perform and validate request
        mockMvc.perform(
            delete("/customers/$nonExistentCustomerId").accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Customer with ID $nonExistentCustomerId not found"))
    }
}