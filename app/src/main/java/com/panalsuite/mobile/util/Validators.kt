package com.panalsuite.mobile.util

import androidx.annotation.StringRes
import com.panalsuite.mobile.R
import com.panalsuite.mobile.domain.model.TicketCategory

object Validators {

    const val TITLE_MAX_LENGTH = 80
    const val DESCRIPTION_MAX_LENGTH = 500

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private const val MIN_PASSWORD_LENGTH = 8
    private const val MAX_COMMENT_LENGTH = 300
    private const val TITLE_MIN_LENGTH = 5
    private const val DESCRIPTION_MIN_LENGTH = 10
    private const val BRANCH_MIN_LENGTH = 3

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

    @StringRes
    fun validateTicketTitle(title: String): Int? = when {
        title.isBlank() -> R.string.error_title_required
        title.trim().length < TITLE_MIN_LENGTH -> R.string.error_title_too_short
        title.trim().length > TITLE_MAX_LENGTH -> R.string.error_title_too_long
        else -> null
    }

    @StringRes
    fun validateTicketDescription(description: String): Int? = when {
        description.isBlank() -> R.string.error_description_required
        description.trim().length < DESCRIPTION_MIN_LENGTH -> R.string.error_description_too_short
        description.trim().length > DESCRIPTION_MAX_LENGTH -> R.string.error_description_too_long
        else -> null
    }

    @StringRes
    fun validateBranch(branch: String): Int? = when {
        branch.isBlank() -> R.string.error_branch_required
        branch.trim().length < BRANCH_MIN_LENGTH -> R.string.error_branch_too_short
        else -> null
    }

    @StringRes
    fun validateCategory(category: TicketCategory?): Int? =
        if (category == null) R.string.error_category_required else null
}