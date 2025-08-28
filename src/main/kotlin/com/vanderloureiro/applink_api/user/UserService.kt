package com.vanderloureiro.applink_api.user

import org.springframework.stereotype.Service
import java.util.UUID

@Service
final class UserService(private val userRepository: UserRepository) {

    fun get(id: UUID): User {
        val user = this.userRepository.findById(id)
        return user.orElseThrow()
    }

    fun getByEmail(email: String): User {
        return userRepository.findByEmail(email).orElseThrow()
    }

    fun create(user: User) {
        user.name = user.email.split('@')[0]
        user.isValidatedEmail = false
        userRepository.save(user)
    }
}