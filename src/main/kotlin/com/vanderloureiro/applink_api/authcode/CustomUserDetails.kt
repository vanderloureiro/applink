package com.vanderloureiro.applink_api.authcode

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CustomUserDetails(
    val id: UUID,
    private val email: String,
    private val passwordHash: String,
    private val authoritiesList: Collection<GrantedAuthority> = listOf()
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authoritiesList

    override fun getPassword(): String = passwordHash

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}