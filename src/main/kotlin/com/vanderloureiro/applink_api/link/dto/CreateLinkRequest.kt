package com.vanderloureiro.applink_api.link.dto

import com.vanderloureiro.applink_api.link.Link
import com.vanderloureiro.applink_api.user.User
import java.util.UUID

data class CreateLinkRequest(
    val title: String,
    val url: String,
    val description: String?
)
