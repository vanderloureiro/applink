package com.vanderloureiro.applink_api.authcode

import com.vanderloureiro.applink_api.authcode.dto.ValidateAuthCodeRequest
import com.vanderloureiro.applink_api.common.exception.UnauthorizedException
import com.vanderloureiro.applink_api.notification.EmailService
import com.vanderloureiro.applink_api.user.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
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
    private val emailService: EmailService,
) {
    private val logger = KotlinLogging.logger {}

    fun generate(email: String) {
        val user = userService.getByEmail(email) ?: throw UnauthorizedException()
        generate(user.id!!)
    }

    fun generate(userId: UUID) {
        val code = RandomGenerator.getDefault().nextInt(100000, 999999).toString()
        val encoded = encoder.encode(code)
        val user = userService.get(userId)
        if (user != null) {
            val authCode = AuthCode(userId = userId, encoded, updatedAt = OffsetDateTime.now())
            val maybeAuthCode = repository.findById(userId);
            if (maybeAuthCode.isPresent && maybeAuthCode.get().updatedAt!!.isAfter(
                    OffsetDateTime.now().minusMinutes(2)
                )
            ) {
                logger.info { "Last authcode sent still within the deadline" }
                return
            }
            repository.save(authCode)
            logger.info { code }
            emailService.sendAuthCodeEmail(code, user.name, user.email)
        }
    }

    fun validate(auth: ValidateAuthCodeRequest): String {
        val user = userService.getByEmail(auth.email)
        val authCode = repository.getValidAuthCode(user?.id!!) ?: throw UnauthorizedException()
        if (!encoder.matches(auth.code, authCode.code)) {
            throw UnauthorizedException()
        }
        val userDetails = customUserDetailsService.loadUserByUsername(auth.email)
        userService.confirmEmailValidation(auth.email)
        return tokenService.generate(userDetails)
    }

    fun getAuthenticatedUser(): CustomUserDetails {
        val auth = SecurityContextHolder.getContext().authentication
        val principal = auth.principal
        return principal as? CustomUserDetails
            ?: throw UnauthorizedException()
    }
}
