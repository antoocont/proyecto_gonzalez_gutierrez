package com.panalsuite.mobile.util

import androidx.annotation.StringRes
import com.panalsuite.mobile.R

object Validators {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private const val MIN_PASSWORD_LENGTH = 8
    private const val MAX_COMMENT_LENGTH = 300

    @StringRes
    fun validateEmail(email: String): Int? = when {
        email.isBlank() -> R.string.error_email_required
        !EMAIL_REGEX.matches(email.trim()) -> R.string.error_email_invalid
        else -> null
    }

    @StringRes
    fun validatePassword(password: String): Int? = when {
        password.isBlank() -> R.string.error_password_required
        password.length < MIN_PASSWORD_LENGTH -> R.string.error_password_too_short
        else -> null
    }

    @StringRes
    fun validateComment(message: String): Int? = when {
        message.isBlank() -> R.string.error_comment_required
        message.trim().length > MAX_COMMENT_LENGTH -> R.string.error_comment_too_long
        else -> null
    }
}