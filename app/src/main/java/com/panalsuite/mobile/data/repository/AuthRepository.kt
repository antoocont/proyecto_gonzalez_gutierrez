package com.panalsuite.mobile.data.repository

import com.panalsuite.mobile.data.local.SessionManager
import com.panalsuite.mobile.data.local.dao.UserDao
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.util.PasswordHasher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface LoginResult {
    data class Success(val user: UserEntity) : LoginResult
    data object InvalidCredentials : LoginResult
}

class AuthRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager,
    private val awaitDatabaseReady: suspend () -> Unit
) {

    val currentUser: Flow<UserEntity?> = sessionManager.userId.map { userId ->
        userId?.let { userDao.findById(it) }
    }

    suspend fun login(email: String, password: String): LoginResult {
        awaitDatabaseReady()
        val user = userDao.findByEmail(email.trim().lowercase())
        if (user == null || user.passwordHash != PasswordHasher.hash(password)) {
            return LoginResult.InvalidCredentials
        }
        sessionManager.saveSession(user.id)
        return LoginResult.Success(user)
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}