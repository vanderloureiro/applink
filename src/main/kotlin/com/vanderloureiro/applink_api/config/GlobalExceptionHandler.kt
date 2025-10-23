package com.vanderloureiro.applink_api.config

import com.vanderloureiro.applink_api.common.exception.UnauthorizedException
import com.vanderloureiro.applink_api.user.exception.EmailAlreadyUsedException
import com.vanderloureiro.applink_api.user.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException::class)
    fun notFoundException(ex: UserNotFoundException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "User not found")
        problemDetail.title = "RESOURCE_NOT_FOUND"
        problemDetail.setProperty("errorCode", HttpStatus.NOT_FOUND.value())
        return problemDetail
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorizedException(ex: UnauthorizedException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.message ?: "Unauthorized")
        problemDetail.title = "UNAUTHORIZED"
        problemDetail.setProperty("errorCode", HttpStatus.UNAUTHORIZED.value())
        return problemDetail
    }

    @ExceptionHandler(EmailAlreadyUsedException::class)
    fun emailAlreadyUsedException(ex: EmailAlreadyUsedException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message ?: "Email already used")
        problemDetail.title = "EMAIL_ALREADY_USED"
        problemDetail.setProperty("errorCode", HttpStatus.CONFLICT.value())
        return problemDetail
    }

    @ExceptionHandler(RuntimeException::class)
    fun internalServerError(ex: RuntimeException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.message ?: "Internal error")
        problemDetail.title = "INTERNAL_ERROR"
        problemDetail.setProperty("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value())
        return problemDetail
    }
}
