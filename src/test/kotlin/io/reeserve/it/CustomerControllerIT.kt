package io.reeserve.it

import io.reeserve.dto.CustomerRequestDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("/db/data.sql")
class CustomerControllerIT @Autowired constructor(
    val restTemplate: TestRestTemplate
) {

    @LocalServerPort
    val port: Int = 0

    private fun getUrl(endpoint: String) = "http://localhost:$port$endpoint"

    @Test
    fun `should return customers when called GET endpoint`() {
        val response: ResponseEntity<String> = restTemplate.getForEntity(getUrl("/customers"), String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    @Sql("/db/clean.sql")
    fun `should add a customer`() {
        val customerRequest = CustomerRequestDTO("John", "Doe")
        val request = HttpEntity(customerRequest)

        val response = restTemplate.postForEntity(getUrl("/customers"), request, String::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    @Sql("/db/clean.sql")
    fun `should not add a customer with invalid data`() {
        val invalidCustomerRequest = CustomerRequestDTO("", "")
        val request = HttpEntity(invalidCustomerRequest)

        val response = restTemplate.postForEntity(getUrl("/customers"), request, String::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `should delete a customer by their ID`() {
        // Assuming there's a customer with ID 1
        val response = restTemplate.exchange(getUrl("/customers/1"), HttpMethod.DELETE, null, String::class.java)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun `should not delete a customer with non-existing ID`() {
        // Assuming there's no customer with ID 999
        val response = restTemplate.exchange(getUrl("/customers/999"), HttpMethod.DELETE, null, String::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}