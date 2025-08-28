package com.vanderloureiro.applink_api.authcode

import com.vanderloureiro.applink_api.user.UserService
import com.vanderloureiro.applink_api.authcode.dto.ValidateAuthCodeRequest
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.random.RandomGenerator

@Service
class AuthService(private val repository: AuthCodeRepository, private val userService: UserService) {

    fun generate(email: String) {
        val user = userService.getByEmail(email)
        generate(user.id!!)
    }

    fun generate(userId: UUID) {
        val code = RandomGenerator.getDefault().nextInt(100000, 999999).toString()
        val user = userService.get(userId)
        val authCode = AuthCode(userId = userId, code)
        repository.save(authCode)

        // sendMail(user, code)
    }

    fun validate(auth: ValidateAuthCodeRequest): Boolean {
        val user = userService.getByEmail(auth.email)
        val authCode = repository.getValidAuthCode(user.id!!, auth.code)
        val isValid = authCode?.code.equals(auth.code)
        return isValid
    }
}