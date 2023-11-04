package io.reeserve.handlers

import io.reeserve.exceptions.ResourceNotFoundException
import io.reeserve.handlers.ErrorResponse.Companion.buildErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.security.access.AccessDeniedException

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleEntityNotFoundException(
        ex: ResourceNotFoundException,
        request: WebRequest
    ): ResponseEntity<Any> {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        ex: AuthenticationException,
        request: WebRequest
    ): ResponseEntity<Any> {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: WebRequest
    ): ResponseEntity<Any> {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex, request)
    }

    // Override the method from ResponseEntityExceptionHandler
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<Any> {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request)
    }
}