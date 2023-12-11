package io.reeserve.dto

import jakarta.validation.constraints.NotBlank
import java.io.Serializable

data class CustomerRequestDTO(
    @field:NotBlank(message = "First name cannot be blank")
    val firstName: String,
    @field:NotBlank(message = "Last name cannot be blank")
    val lastName: String
) : Serializable
