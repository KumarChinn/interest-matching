package com.cerqlar.intmatch.utils.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Created by chinnku on Aug, 2021
 * ErrorDetails
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(exception: Exception): ResponseEntity<ErrorDetails> {
        val error = ErrorDetails(exception.message)
        return ResponseEntity(error, error.status)
    }
    @ExceptionHandler(EntityNotSavedException::class)
    fun EntityNotSavedExceptionHandler(exception: Exception): ResponseEntity<ErrorDetails> {
        val error = ErrorDetails(exception.message,HttpStatus.INTERNAL_SERVER_ERROR)
        return ResponseEntity(error, error.status)
    }
    @ExceptionHandler(ResourceNotFoundException::class)
    fun ResourceNotFoundExceptionHandler(exception: Exception): ResponseEntity<ErrorDetails> {
        val error = ErrorDetails(exception.message,HttpStatus.INTERNAL_SERVER_ERROR)
        return ResponseEntity(error, error.status)
    }
    @ExceptionHandler(NoMatchFoundException::class)
    fun NoMatchFoundExceptionHandler(exception: Exception): ResponseEntity<ErrorDetails> {
        val error = ErrorDetails(exception.message,HttpStatus.CONFLICT)
        return ResponseEntity(error, error.status)
    }
}