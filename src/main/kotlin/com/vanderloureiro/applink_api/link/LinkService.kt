package com.vanderloureiro.applink_api.link

import org.springframework.stereotype.Service

@Service
class LinkService(private val userRepository: LinkRepository) {

    fun create(link: Link) {
        userRepository.save(link)
    }

    fun get(): List<Link> {
        return this.userRepository.findAll()
    }
}