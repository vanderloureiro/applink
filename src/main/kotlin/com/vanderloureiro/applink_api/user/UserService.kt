package com.vanderloureiro.applink_api.user

import com.vanderloureiro.applink_api.common.exception.UnauthorizedException
import com.vanderloureiro.applink_api.user.dto.CreateUserRequest
import com.vanderloureiro.applink_api.user.exception.EmailAlreadyUsedException
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

@Service
final class UserService(
    private val userRepository: UserRepository,
) {
    fun get(id: UUID): User? = this.userRepository.findById(id).getOrNull()

    fun getByEmail(email: String): User? = userRepository.findByEmail(email)

    fun create(request: CreateUserRequest) {
        if (!request.termsAccepted) {
            throw UnauthorizedException()
        }
        
        val user = request.toDomain()
        val maybeUser = userRepository.findByEmail(user.email)
        if (maybeUser != null) {
            throw EmailAlreadyUsedException()
        }
        user.name = user.email.substringBefore('@') + generateRandomNumbers(6)
        user.isValidatedEmail = false
        userRepository.save(user)
    }

    fun confirmEmailValidation(email: String) {
        userRepository.changeEmailValidation(email, true)
    }

    fun generateRandomNumbers(qtd: Int): String =
        (1..qtd)
            .map { Random.nextInt(0, 10) }
            .joinToString("")
}
