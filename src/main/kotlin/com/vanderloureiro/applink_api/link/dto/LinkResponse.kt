package com.vanderloureiro.applink_api.link.dto

import java.time.OffsetDateTime
import java.util.UUID

data class LinkResponse(
    val id: UUID,
    val title: String,
    val url: String,
    val description: String?,
    val createdAt: OffsetDateTime,
)
