package io.reeserve.handlers

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@Schema(description = "Details about the error")
data class ErrorResponse(
    @Schema(description = "Timestamp when the error occurred")
    val timestamp: LocalDateTime,

    @Schema(description = "HTTP status code")
    val status: Int,

    @Schema(description = "Error type")
    val error: String,

    @Schema(description = "Detailed error message")
    val message: String,

    @Schema(description = "API path where error occurred")
    val path: String
) {
    companion object {
        private fun buildErrorResponseInternal(
            status: HttpStatus,
            message: String? = null,
            path: String,
            exception: Exception
        ): ResponseEntity<Any> {
            val errorResponse = ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = status.value(),
                error = status.name,
                message = message ?: exception.message ?: "Unexpected error",
                path = path
            )
            return ResponseEntity(errorResponse, status)
        }

        fun buildErrorResponse(
            status: HttpStatus,
            exception: Exception,
            request: WebRequest,
            message: String? = null
        ): ResponseEntity<Any> {
            return buildErrorResponseInternal(
                status = status,
                exception = exception,
                message = message,
                path = request.getDescription(false)
            )
        }

        fun buildErrorResponse(
            status: HttpStatus,
            exception: Exception,
            request: HttpServletRequest,
            message: String? = null
        ): ResponseEntity<Any> {
            return buildErrorResponseInternal(
                status = status,
                exception = exception,
                message = message,
                path = request.requestURI
            )
        }
    }
}
