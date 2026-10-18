package com.panalsuite.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SessionState {
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data class LoggedIn(val user: UserEntity) : SessionState
}

private fun sessionStateOf(user: UserEntity?): SessionState =
    if (user == null) SessionState.LoggedOut else SessionState.LoggedIn(user)

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val sessionState: StateFlow<SessionState> = authRepository.currentUser
        .map { user -> sessionStateOf(user) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SessionState.Loading
        )

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }

    companion object {
        fun factory(authRepository: AuthRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { MainViewModel(authRepository) }
        }
    }
}