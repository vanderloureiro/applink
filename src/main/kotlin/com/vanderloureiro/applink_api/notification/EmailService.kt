package com.vanderloureiro.applink_api.notification

import io.mailtrap.config.MailtrapConfig
import io.mailtrap.factory.MailtrapClientFactory
import io.mailtrap.model.request.emails.Address
import io.mailtrap.model.request.emails.MailtrapMail
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.List

@Service
class EmailService(
    @Value("\${mail.recipient}") private val recipient: String,
    @Value("\${mail.token}") private val token: String,
) {
    fun sendAuthCodeEmail(
        code: String,
        name: String,
        email: String,
    ) {
        val inboxId = 3521491L
        val config =
            MailtrapConfig
                .Builder()
                .token(token)
                .build()

        val client = MailtrapClientFactory.createMailtrapClient(config)

        val mail =
            MailtrapMail
                .builder()
                .from(Address(recipient, "Ibira.cc"))
                .to(List.of<Address?>(Address(email)))
                .replyTo(Address(recipient, "Ibira"))
                .subject("Ibira - Entrar")
                .text("Olá, $name. O seu código de autenticação é: $code")
                .build()

        try {
            // TODO: change
            // client.switchToEmailTestingApi(inboxId)
            println(client.send(mail))
        } catch (e: Exception) {
            println("Caught exception : " + e)
        }
    }
}
