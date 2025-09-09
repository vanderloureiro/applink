package com.vanderloureiro.applink_api.user

import com.vanderloureiro.applink_api.user.dto.CreateUserRequest
import com.vanderloureiro.applink_api.user.dto.UserResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["http://localhost:3000"])
@Tags(Tag(name = "User", description = "User resources"))
class UserController(val userService: UserService) {

    @PostMapping
    @ApiResponse(description = "Create an user")
    fun create(@RequestBody request: CreateUserRequest): ResponseEntity<Void> {
        userService.create(request.toDomain())
        return ResponseEntity.noContent().build()
    }

    @ApiResponse(description = "Finds an user by your id")
    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<UserResponse> {
        val response = UserResponse.fromDomain(userService.get(id))
        return ResponseEntity.ok(response)
    }
}