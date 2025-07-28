package com.vanderloureiro.applink_api.link.dto

import com.vanderloureiro.applink_api.link.Link

data class CreateLinkRequest(val title: String,
                             val url: String,
                             val description: String?) {

    fun toModel(): Link {
        return Link(title = this.title, path = this.url, description = this.description)
    }
}
