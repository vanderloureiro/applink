package com.vanderloureiro.applink_api.link

import com.vanderloureiro.applink_api.link.dto.CreateLinkRequest
import com.vanderloureiro.applink_api.link.dto.LinkResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/links")
@CrossOrigin(origins = ["http://localhost:3000"])
@Tags(Tag(name = "Link", description = "Link resources"))
class LinkController(private val linkService: LinkService) {

    @PostMapping
    fun create(@RequestBody request: CreateLinkRequest): ResponseEntity<Void> {
        val link = request.toDomain()
        this.linkService.create(link)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun get(query: String = "", @PageableDefault(page = 0, size = 3, direction = Sort.Direction.ASC) pageable: Pageable): List<LinkResponse> {
        val links = this.linkService.get(query, pageable)
        return links.map { item ->
            LinkResponse(
                id = item.id!!,
                title = item.title,
                url = item.path,
                description = item.description,
                createdAt = item.createdAt!!
            )
        }
    }
}