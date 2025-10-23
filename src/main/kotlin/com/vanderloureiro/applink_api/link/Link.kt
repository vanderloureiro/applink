package com.vanderloureiro.applink_api.link

import com.vanderloureiro.applink_api.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "links")
class Link(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var title: String = "",
    var path: String = "",
    var description: String? = null,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var owner: User,
    @Column(name = "created_at")
    @CreationTimestamp
    var createdAt: OffsetDateTime? = null,
) {
//
//    @Column(name = "updated_at")
//    var updatedAt: OffsetDateTime? = null
}
