package com.panalsuite.mobile.ui.screens.login

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.panalsuite.mobile.data.repository.AuthRepository
import com.panalsuite.mobile.data.repository.LoginResult
import com.panalsuite.mobile.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    val isLoading: Boolean = false,
    val loginError: Boolean = false
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, loginError = false) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, loginError = false) }
    }

    fun onDemoAccountSelected(account: DemoAccount) {
        _uiState.update {
            it.copy(
                email = account.email,
                password = DemoAccounts.PASSWORD,
                emailError = null,
                passwordError = null,
                loginError = false
            )
        }
    }

    fun onLoginClick() {
        val current = _uiState.value
        if (current.isLoading) return

        val emailError = Validators.validateEmail(current.email)
        val passwordError = Validators.validatePassword(current.password)
        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = false) }
            when (authRepository.login(current.email, current.password)) {
                is LoginResult.Success -> _uiState.value = LoginUiState()
                LoginResult.InvalidCredentials ->
                    _uiState.update { it.copy(isLoading = false, loginError = true) }
            }
        }
    }

    companion object {
        fun factory(authRepository: AuthRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { LoginViewModel(authRepository) }
        }
    }
}