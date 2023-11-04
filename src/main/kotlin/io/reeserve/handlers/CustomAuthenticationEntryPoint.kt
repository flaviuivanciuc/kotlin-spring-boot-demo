package io.reeserve.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.reeserve.handlers.ErrorResponse.Companion.buildErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: AuthenticationException
    ) {
        val errorResponseEntity = buildErrorResponse(
            status = HttpStatus.UNAUTHORIZED,
            exception = ex,
            request = request,
            message = ex.message
        )

        // Convert the error response to JSON
        val json = objectMapper.writeValueAsString(errorResponseEntity.body)

        response.status = errorResponseEntity.statusCode.value()
        response.contentType = "application/json"
        response.writer.write(json)
    }
}
