package com.vanderloureiro.applink_api.authcode

import com.vanderloureiro.applink_api.authcode.dto.ValidateAuthCodeRequest
import com.vanderloureiro.applink_api.user.User
import com.vanderloureiro.applink_api.user.UserService
import com.vanderloureiro.applink_api.user.exception.UserNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*
import java.util.random.RandomGenerator

@Service
class AuthService(
    private val repository: AuthCodeRepository,
    private val userService: UserService,
    private val tokenService: TokenService,
    private val customUserDetailsService: CustomUserDetailsService,
    private val encoder: PasswordEncoder,
    private val authManager: AuthenticationManager) {

    private val logger = KotlinLogging.logger {}

    // [Refatorar] Preciso abrir o método pra entender regra de negócio e validação
    fun generate(email: String) {
        val user = userService.getByEmail(email) ?: throw UserNotFoundException()
        generate(user.id!!)
    }

    fun generate(userId: UUID) {
        val code = RandomGenerator.getDefault().nextInt(100000, 999999).toString()
        val encoded = encoder.encode(code)
        val user = userService.get(userId)
        if (user != null) {
            val authCode = AuthCode(userId = userId, encoded, updatedAt = OffsetDateTime.now())
            repository.save(authCode)
            logger.info { code }
        }
        // sendMail(user, code)
    }

    fun validate(auth: ValidateAuthCodeRequest): String? {
        val user = userService.getByEmail(auth.email)
        val authCode = repository.getValidAuthCode(user?.id!!) ?: return null
        if (!encoder.matches(auth.code, authCode.code)) {
            return null
        }
        val userDetails = customUserDetailsService.loadUserByUsername(auth.email)
        userService.confirmEmailValidation(auth.email)
        return tokenService.generate(userDetails)
    }

    fun getAuthenticatedUser(): CustomUserDetails? {
        val auth = SecurityContextHolder.getContext().authentication
        val principal = auth.principal
        return principal as? CustomUserDetails
            ?: throw IllegalStateException("Unexpected principal type: ${principal::class.simpleName}")
    }



}