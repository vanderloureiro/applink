package com.vanderloureiro.applink_api.link.dto

import com.vanderloureiro.applink_api.link.Link

data class CreateLinkRequest(val title: String,
                             val path: String,
                             val description: String?) {

    fun toModel(): Link {
        return Link(title = this.title, path = this.path, description = this.description)
    }
}
