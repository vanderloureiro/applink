package com.vanderloureiro.applink_api.notification

interface EmailService {
    fun sendAuthCodeEmail(
        code: String,
        name: String,
        email: String,
    )
}
