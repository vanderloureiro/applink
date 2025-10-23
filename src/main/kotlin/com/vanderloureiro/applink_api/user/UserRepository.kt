package com.vanderloureiro.applink_api.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isValidatedEmail = :isValidated WHERE u.email = :email")
    fun changeEmailValidation(
        email: String,
        isValidated: Boolean,
    ): Int
}
