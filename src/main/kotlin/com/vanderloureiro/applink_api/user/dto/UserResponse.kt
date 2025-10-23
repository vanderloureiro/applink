package com.vanderloureiro.applink_api.user.dto

import com.vanderloureiro.applink_api.user.User
import java.time.OffsetDateTime
import java.util.UUID

data class UserResponse(
    var id: UUID? = null,
    var name: String = "",
    var email: String = "",
    var isValidatedEmail: Boolean? = false,
    var createdAt: OffsetDateTime? = null,
) {
    companion object {
        fun fromDomain(user: User): UserResponse =
            UserResponse(
                id = user.id,
                name = user.name,
                email = user.email,
                isValidatedEmail = user.isValidatedEmail,
                createdAt = user.createdAt,
            )
    }
}
