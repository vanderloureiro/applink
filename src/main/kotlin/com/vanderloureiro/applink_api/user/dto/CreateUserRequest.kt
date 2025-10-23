package com.vanderloureiro.applink_api.user.dto

import com.vanderloureiro.applink_api.user.User

data class CreateUserRequest(
    val email: String,
) {
    fun toDomain(): User = User(email = email, name = email)
}
