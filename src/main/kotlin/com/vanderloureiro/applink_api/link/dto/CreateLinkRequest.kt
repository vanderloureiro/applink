package com.vanderloureiro.applink_api.link.dto

import com.vanderloureiro.applink_api.link.Link
import com.vanderloureiro.applink_api.user.User
import java.util.UUID

data class CreateLinkRequest(
    val title: String,
    val url: String,
    val description: String?,
    val ownerId: UUID
) {

    fun toDomain(): Link {
        return Link(title = this.title, path = this.url, description = this.description, owner = User(id = ownerId))
    }
}
