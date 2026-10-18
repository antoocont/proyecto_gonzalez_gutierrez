package com.panalsuite.mobile.util

import com.panalsuite.mobile.R

object Validators {
    fun validateEmail(email: String): Int? {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) return R.string.error_invalid_email
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        return if (emailRegex.matches(trimmed)) null else R.string.error_invalid_email
    }

    fun validatePassword(password: String): Int? {
        return if (password.length >= 4) null else R.string.error_invalid_password
    }
}
