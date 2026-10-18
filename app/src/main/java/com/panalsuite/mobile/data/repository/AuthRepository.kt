package com.panalsuite.mobile.data.repository

import com.panalsuite.mobile.data.local.dao.UserDao
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.util.PasswordHasher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface LoginResult {
    object Success : LoginResult
    object InvalidCredentials : LoginResult
}

class AuthRepository(private val userDao: UserDao) {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    suspend fun login(email: String, password: String): LoginResult {
        val user = userDao.findByEmail(email.trim().lowercase()) ?: return LoginResult.InvalidCredentials
        val hashed = PasswordHasher.hash(password)
        return if (user.passwordHash == hashed) {
            _currentUser.value = user
            LoginResult.Success
        } else {
            LoginResult.InvalidCredentials
        }
    }

    fun logout() {
        _currentUser.value = null
    }
}
