package com.vanderloureiro.applink_api.user

import com.vanderloureiro.applink_api.user.exception.EmailAlreadyUsedException
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
final class UserService(private val userRepository: UserRepository) {

    fun get(id: UUID): User? = this.userRepository.findById(id).getOrNull()

    fun getByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun create(user: User) {
        val maybeUser = userRepository.findByEmail(user.email)
        if (maybeUser != null) {
            throw EmailAlreadyUsedException()
        }
        /*
        * Validar isso depois. Pode haver user duplicado em casos de mesmo nome de email mas de provedores diferentes
        * */
        user.name = user.email.split('@')[0]
        user.isValidatedEmail = false
        userRepository.save(user)
    }
}