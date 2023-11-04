package io.reeserve.integration_tests

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reeserve.dto.CustomerRequestDTO
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("/db/data.sql")
class CustomerControllerIT @Autowired constructor(
    val mockMvc: MockMvc
) {

    @Test
    @WithMockUser(username = "testUser", roles = ["client_admin"])
    fun `should return customers when called GET endpoint`() {
        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
    }

    @Test
    @Sql("/db/clean.sql")
    @WithMockUser(username = "testUser", roles = ["client_user"])
    fun `should add a customer`() {
        val customerRequest = CustomerRequestDTO("John", "Doe")
        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerRequest))
        )
            .andExpect(status().isCreated)
    }

    @Test
    @Sql("/db/clean.sql")
    @WithMockUser(username = "testUser", roles = ["client_user"])
    fun `should not add a customer with invalid data`() {
        val invalidCustomerRequest = CustomerRequestDTO("", "")
        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidCustomerRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "testUser", roles = ["client_admin"])
    fun `should delete a customer by their ID`() {
        // Assuming there's a customer with ID 1
        mockMvc.perform(delete("/customers/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithMockUser(username = "testUser", roles = ["client_admin"])
    fun `should not delete a customer with non-existing ID`() {
        // Assuming there's no customer with ID 999
        mockMvc.perform(delete("/customers/999"))
            .andExpect(status().isNotFound)
    }

    // Helper method to convert objects to JSON string
    fun asJsonString(obj: Any): String {
        return jacksonObjectMapper().writeValueAsString(obj)
    }
}
