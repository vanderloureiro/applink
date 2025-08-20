package com.vanderloureiro.applink_api.user

import com.vanderloureiro.applink_api.link.Link
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var name: String = "",
    var email: String = "",
    @Column(name = "is_validated_email")
    var isValidatedEmail: Boolean? = false,
    @OneToMany
    val links: MutableList<Link> = mutableListOf(),
    @Column(name = "created_at")
    @CreationTimestamp
    var createdAt: OffsetDateTime? = null
) {
//
//    @Column(name = "updated_at")
//    var updatedAt: OffsetDateTime? = null

}
