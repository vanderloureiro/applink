package com.vanderloureiro.applink_api.link

import com.vanderloureiro.applink_api.link.dto.CreateLinkRequest
import com.vanderloureiro.applink_api.link.dto.LinkResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/links")
@CrossOrigin(origins = ["http://localhost:3000"])
class LinkController(val linkRepository: LinkRepository) {

    @PostMapping
    fun create(@RequestBody request: CreateLinkRequest, @RequestHeader("api-key") apiKey: String): ResponseEntity<Void> {
        val link = request.toModel()
        this.linkRepository.save(link)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun get(): List<LinkResponse> {
        val links = this.linkRepository.findAll()
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