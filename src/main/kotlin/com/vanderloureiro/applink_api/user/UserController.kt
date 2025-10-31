package com.vanderloureiro.applink_api.user

import com.vanderloureiro.applink_api.authcode.AuthService
import com.vanderloureiro.applink_api.user.dto.CreateUserRequest
import com.vanderloureiro.applink_api.user.dto.UserResponse
import com.vanderloureiro.applink_api.user.exception.UserNotFoundException
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["\${app.cors.origin:http://localhost:3000}"])
@Tags(Tag(name = "User", description = "User resources"))
class UserController(
    private val userService: UserService,
    private val authService: AuthService,
) {
    @PostMapping
    @ApiResponse(description = "Create an user")
    fun create(
        @RequestBody request: CreateUserRequest,
    ): ResponseEntity<Void> {
        userService.create(request.toDomain())
        return ResponseEntity.noContent().build()
    }

    @ApiResponse(description = "Finds an user by your id")
    @GetMapping("/me")
    fun get(): ResponseEntity<UserResponse> {
        val id = authService.getAuthenticatedUser().id
        val user = userService.get(id) ?: throw UserNotFoundException()
        val response = UserResponse.fromDomain(user)
        return ResponseEntity.ok(response)
    }
}
