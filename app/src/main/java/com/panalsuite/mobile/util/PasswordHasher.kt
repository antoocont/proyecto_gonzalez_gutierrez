package com.panalsuite.mobile.util

import java.security.MessageDigest

object PasswordHasher {

    fun hash(plaintext: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(plaintext.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}