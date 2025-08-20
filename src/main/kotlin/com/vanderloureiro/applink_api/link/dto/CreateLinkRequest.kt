package com.vanderloureiro.applink_api.link.dto

import com.vanderloureiro.applink_api.link.Link
import com.vanderloureiro.applink_api.user.User

data class CreateLinkRequest(val title: String,
                             val url: String,
                             val description: String?) {

    fun toModel(): Link {
        return Link(title = this.title, path = this.url, description = this.description, owner = User(name = "Test", email = "test@email"))
    }
}
