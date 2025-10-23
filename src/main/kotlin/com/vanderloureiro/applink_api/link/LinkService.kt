package com.vanderloureiro.applink_api.link

import com.vanderloureiro.applink_api.authcode.CustomUserDetails
import com.vanderloureiro.applink_api.common.exception.UnauthorizedException
import com.vanderloureiro.applink_api.link.dto.CreateLinkRequest
import com.vanderloureiro.applink_api.user.User
import com.vanderloureiro.applink_api.user.UserService
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.util.UUID

@Service
class LinkService(private val linkRepository: LinkRepository, private val userService: UserService) {

    fun create(request: CreateLinkRequest, auth: CustomUserDetails) {
        val user = userService.get(auth.id)!!
        val link = Link(title = request.title, path = request.url, description = request.description, owner = user)
        linkRepository.save(link)
    }

    fun get(search: String = "", owner: UUID, pageable: Pageable): List<Link> {
        val spec = Specification<Link> { root, _, builder ->
            val predicates = mutableListOf<Predicate>()

            if (search.isNotBlank()) {
                predicates.add(
                    builder.like(
                        builder.lower(root.get("title")),
                        "%${search.lowercase()}%"
                    )
                )
            }

            predicates.add(
                builder.equal(root.get<User>("owner").get<UUID>("id"), owner)
            )

            builder.and(*predicates.toTypedArray())
        }
        return this.linkRepository.findAll(spec, pageable).toList()
    }

    fun remove(linkId: UUID, owner: UUID) {
        val link = linkRepository.findById(linkId)
        if (link.isEmpty) return
        if (link.get().owner.id != owner) {
            throw UnauthorizedException()
        }
        linkRepository.deleteById(linkId)
    }
}