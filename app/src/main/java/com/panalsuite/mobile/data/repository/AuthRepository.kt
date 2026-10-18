package com.panalsuite.mobile.data.repository

import com.panalsuite.mobile.data.local.dao.UserDao
import com.panalsuite.mobile.util.PasswordHasher

sealed interface LoginResult {
    object Success : LoginResult
    object InvalidCredentials : LoginResult
}

class AuthRepository(private val userDao: UserDao) {
    suspend fun login(email: String, password: String): LoginResult {
        val user = userDao.findByEmail(email.trim().lowercase()) ?: return LoginResult.InvalidCredentials
        val hashed = PasswordHasher.hash(password)
        return if (user.passwordHash == hashed) {
            LoginResult.Success
        } else {
            LoginResult.InvalidCredentials
        }
    }
}
