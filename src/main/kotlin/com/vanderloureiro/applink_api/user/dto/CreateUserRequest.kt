package com.vanderloureiro.applink_api.user.dto

import com.vanderloureiro.applink_api.user.User

data class CreateUserRequest(val email: String) {
    fun toDomain(): User {
        return User(email = email, name = email)
    }
}
