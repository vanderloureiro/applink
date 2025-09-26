package com.vanderloureiro.applink_api.link

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class LinkService(private val userRepository: LinkRepository) {

    fun create(link: Link) {
        userRepository.save(link)
    }

    fun get(search: String = "", pageable: Pageable): List<Link> {
        val spec = Specification<Link> {
            root, query, builder -> builder.like(builder.lower(root.get("title")), "%${search.lowercase()}%")
        }
        return this.userRepository.findAll(spec, pageable).toList()
    }
}