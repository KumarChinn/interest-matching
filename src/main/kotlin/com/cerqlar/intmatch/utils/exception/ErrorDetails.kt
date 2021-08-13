package com.cerqlar.intmatch.utils.exception

import org.springframework.http.HttpStatus

/**
 * Created by chinnku on Aug, 2021
 * ErrorDetails
 */
data class ErrorDetails(
    private val _message: String?,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
    val code: Int = status.value()
) {
    val message: String
        get() = _message ?: "Something went wrong"
}
