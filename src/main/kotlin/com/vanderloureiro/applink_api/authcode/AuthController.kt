package com.vanderloureiro.applink_api.authcode

import com.vanderloureiro.applink_api.authcode.dto.SignInRequest
import com.vanderloureiro.applink_api.authcode.dto.ValidateAuthCodeRequest
import com.vanderloureiro.applink_api.authcode.dto.ValidateResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["http://localhost:3000"])
@Tags(Tag(name = "Auth", description = "Auth resource"))
class AuthController(private val authService: AuthService) {


    @PostMapping("/sign-in")
    fun sighIn(@RequestBody req: SignInRequest) {
        authService.generate(req.email)
    }

    @PostMapping("/validate")
    fun validate(@RequestBody req: ValidateAuthCodeRequest): ResponseEntity<ValidateResponse> {
        val token = authService.validate(req)
            ?: return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .build()

        return ResponseEntity.ok(ValidateResponse(token))
    }
}