package br.challenge.smartcashflowapp.controllers.exception

import br.challenge.smartcashflowapp.dtos.exceptions.BadRequestException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: MethodArgumentNotValidException): ErrorResponse {
        val errors = ex.bindingResult.allErrors.map {
            val fieldName = (it as FieldError).field
            val errorMessage = it.defaultMessage
            ErrorDetail(fieldName, errorMessage)
        }
        return ErrorResponse("Validation failed", errors)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: MethodArgumentTypeMismatchException): ErrorResponse {
        return ErrorResponse("Validation failed", listOf(ErrorDetail("Method Argument Type Mismatch Exception", ex.message)))
    }

    @ExceptionHandler(InvalidFormatException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: InvalidFormatException): ErrorResponse {
        return ErrorResponse("Validation failed", listOf(ErrorDetail("Invalid Format Exception", ex.message)))
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: BadRequestException): ErrorResponse {
        return ErrorResponse("Validation failed", listOf(ErrorDetail("Bad request", ex.message)))
    }

    data class ErrorResponse(val message: String, val errors: List<ErrorDetail>?)

    data class ErrorDetail(val fieldName: String, val message: String?)
}