package com.vanderloureiro.applink_api.authcode

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuthCodeRepository : JpaRepository<AuthCode, UUID> {

    @Query(
        value = "SELECT * FROM auth_code WHERE user_id = :userId AND code = :code AND created_at >= NOW() - INTERVAL '4 HOUR'",
        nativeQuery = true
    )
    fun getValidAuthCode(@Param("userId") userId: UUID, @Param("code") code: String): AuthCode?
}