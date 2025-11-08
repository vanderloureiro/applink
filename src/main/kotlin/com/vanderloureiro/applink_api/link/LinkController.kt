package com.vanderloureiro.applink_api.link

import com.vanderloureiro.applink_api.authcode.AuthService
import com.vanderloureiro.applink_api.link.dto.CreateLinkRequest
import com.vanderloureiro.applink_api.link.dto.LinkListResponse
import com.vanderloureiro.applink_api.link.dto.LinkResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/links")
@CrossOrigin(origins = ["\${app.cors.origin:http://localhost:3000}"])
@Tags(Tag(name = "Link", description = "Link resources"))
class LinkController(
    private val linkService: LinkService,
    private val authService: AuthService,
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @RequestBody request: CreateLinkRequest,
    ): ResponseEntity<Void> {
        val userDetails = authService.getAuthenticatedUser()
        this.linkService.create(request, userDetails)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun get(
        query: String = "",
        @PageableDefault(page = 0, size = 3, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ResponseEntity<LinkListResponse> {
        val authUser = authService.getAuthenticatedUser()
        val links = this.linkService.get(query, authUser.id, pageable)
        val mapped =
            links.map { item ->
                LinkResponse(
                    id = item.id!!,
                    title = item.title,
                    url = item.path,
                    description = item.description,
                    createdAt = item.createdAt!!,
                )
            }
        val response =
            LinkListResponse(
                content = mapped.get().toList(),
                pageNumber = links.pageable.pageNumber,
                pageSize = links.pageable.pageSize,
                totalPage = links.totalPages,
                totalElements = links.totalElements,
                empty = links.isEmpty,
            )
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{linkId}")
    fun remove(
        @PathVariable linkId: UUID,
    ): ResponseEntity<Void> {
        val authUser = authService.getAuthenticatedUser()
        linkService.remove(linkId, authUser.id)
        return ResponseEntity.noContent().build()
    }
}
