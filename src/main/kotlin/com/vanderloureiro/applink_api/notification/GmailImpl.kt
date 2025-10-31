package com.vanderloureiro.applink_api.notification

import org.springframework.context.annotation.Primary
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Primary
@Service
class GmailImpl(
    private val mailSender: JavaMailSender,
) : EmailService {
    override fun sendAuthCodeEmail(
        code: String,
        name: String,
        email: String,
    ) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.subject = "AppLink - Entrar"
        message.text = "Olá, $name. O seu código de autenticação é: $code"
        message.from = "vanderloureiroleite@gmail.com"

        mailSender.send(message)
    }
}
