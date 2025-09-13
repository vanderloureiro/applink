package com.vanderloureiro.applink_api.authcode

import com.vanderloureiro.applink_api.authcode.dto.ValidateAuthCodeRequest
import com.vanderloureiro.applink_api.user.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*
import java.util.random.RandomGenerator

@Service
class AuthService(private val repository: AuthCodeRepository, private val userService: UserService, private val tokenService: TokenService, private val customUserDetailsService: CustomUserDetailsService) {

    private val logger = KotlinLogging.logger {}

    fun generate(email: String) {
        val user = userService.getByEmail(email)
        generate(user?.id!!)
    }

    fun generate(userId: UUID) {
        val code = RandomGenerator.getDefault().nextInt(100000, 999999).toString()
        val user = userService.get(userId)
        if (user != null) {
            val authCode = AuthCode(userId = userId, code, updatedAt = OffsetDateTime.now())
            repository.save(authCode)
            logger.info { authCode.toString() }
        }
        // sendMail(user, code)
    }

    fun validate(auth: ValidateAuthCodeRequest): String? {
        val user = userService.getByEmail(auth.email)
        val authCode = repository.getValidAuthCode(user?.id!!, auth.code)
        val isValid = authCode?.code.equals(auth.code)
        val userDetails = customUserDetailsService.loadUserByUsername(auth.email)
        if (!isValid) {
            return null
        }
        return tokenService.generate(userDetails)
    }



}