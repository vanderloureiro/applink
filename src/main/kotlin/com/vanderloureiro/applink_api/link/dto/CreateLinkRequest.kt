package com.vanderloureiro.applink_api.link.dto

data class CreateLinkRequest(
    val title: String,
    val url: String,
    val description: String?,
)
