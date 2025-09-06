package com.vanderloureiro.applink_api.link.dto

import com.vanderloureiro.applink_api.link.Link
import com.vanderloureiro.applink_api.user.User
import java.util.UUID

data class CreateLinkRequest(
    val title: String,
    val url: String,
    val description: String?
) {

    fun toDomain(): Link {
        return Link(title = this.title, path = this.url, description = this.description, owner = User(id = UUID.fromString("9dbdb559-55b6-4d64-88b7-7c93c88190bd")))
    }
}
