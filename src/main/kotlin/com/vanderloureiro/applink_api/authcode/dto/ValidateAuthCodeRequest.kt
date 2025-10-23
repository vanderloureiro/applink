package com.vanderloureiro.applink_api.authcode.dto

data class ValidateAuthCodeRequest(
    val email: String,
    val code: String,
)
