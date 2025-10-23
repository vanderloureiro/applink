package com.vanderloureiro.applink_api.authcode

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "auth_code")
data class AuthCode(
    @Id
    @Column(name = "user_id", unique = true)
    var userId: UUID,
    var code: String,
    @Column(name = "updated_at")
    @CreationTimestamp
    var updatedAt: OffsetDateTime? = null,
)
