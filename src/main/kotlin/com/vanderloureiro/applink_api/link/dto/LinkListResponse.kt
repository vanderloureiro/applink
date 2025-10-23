package com.vanderloureiro.applink_api.link.dto

data class LinkListResponse(
    val content: List<LinkResponse>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPage: Int,
    val totalElements: Long,
    val empty: Boolean)
