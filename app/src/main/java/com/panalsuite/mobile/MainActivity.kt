package com.panalsuite.mobile

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panalsuite.mobile.ui.MainViewModel
import com.panalsuite.mobile.ui.SessionState
import com.panalsuite.mobile.ui.components.LoadingScreen
import com.panalsuite.mobile.ui.screens.home.HomePlaceholderScreen
import com.panalsuite.mobile.ui.screens.login.LoginScreen
import com.panalsuite.mobile.ui.screens.login.LoginViewModel
import com.panalsuite.mobile.ui.theme.PanalSuiteMobileTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        val authRepository = (application as PanalSuiteApp).container.authRepository

        setContent {
            PanalSuiteMobileTheme {
                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModel.factory(authRepository)
                )
                val sessionState by mainViewModel.sessionState.collectAsStateWithLifecycle()

                when (val state = sessionState) {
                    SessionState.Loading -> LoadingScreen()
                    SessionState.LoggedOut -> {
                        val loginViewModel: LoginViewModel = viewModel(
                            factory = LoginViewModel.factory(authRepository)
                        )
                        LoginScreen(viewModel = loginViewModel)
                    }
                    is SessionState.LoggedIn -> HomePlaceholderScreen(
                        user = state.user,
                        onLogout = mainViewModel::logout
                    )
                }
            }
        }
    }
}