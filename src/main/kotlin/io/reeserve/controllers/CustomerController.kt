package io.reeserve.controllers

import io.reeserve.dto.CustomerRequestDTO
import io.reeserve.dto.CustomerResponseDTO
import io.reeserve.handlers.ErrorResponse
import io.reeserve.services.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Customer")
@RequestMapping(path = ["/customers"])
@ApiResponses(
    value = [
        ApiResponse(
            responseCode = "401", description = "Unauthorized",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "403", description = "Forbidden",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "500", description = "Internal server error",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    ]
)
class CustomerController(
    private val customerService: CustomerService
) {

    private final val logger = LoggerFactory.getLogger(CustomerController::class.java)

    @GetMapping("/{id}")
    @Operation(description = "Retrieve a customer by their ID")
    @PreAuthorize("hasRole('client_admin')")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved customer")
        ]
    )
    fun getCustomer(
        @PathVariable id: Long,
        @ParameterObject
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<CustomerResponseDTO> {
        logger.info("Retrieving customer with ID $id ...")
        return ResponseEntity(customerService.getCustomer(id), HttpStatus.OK)
    }

    @GetMapping
    @Operation(description = "Retrieve all the customers")
    @PreAuthorize("hasRole('client_admin')")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved customers"),
            ApiResponse(
                responseCode = "404", description = "Customer not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun getCustomers(
        @ParameterObject
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<CustomerResponseDTO>> {
        logger.info("Retrieving all customers...")
        return ResponseEntity(customerService.getAllCustomers(pageable), HttpStatus.OK)
    }

    @PostMapping
    @Operation(description = "Add a customer")
    @PreAuthorize("hasRole('client_user')")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Customer added successfully"),
            ApiResponse(
                responseCode = "400", description = "Bad request",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun addCustomer(@Valid @RequestBody customerRequest: CustomerRequestDTO): ResponseEntity<CustomerResponseDTO> {
        logger.info("Add Customer request: $customerRequest")
        return ResponseEntity(customerService.addCustomer(customerRequest), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete a customer by their ID")
    @PreAuthorize("hasRole('client_admin')")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            ApiResponse(
                responseCode = "404", description = "Customer not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun deleteCustomer(@PathVariable id: Long): ResponseEntity<String> {
        customerService.deleteCustomer(id)
        logger.info("Deleted customer with ID $id")
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}