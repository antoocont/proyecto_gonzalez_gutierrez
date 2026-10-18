package com.panalsuite.mobile

import android.app.Application
import com.panalsuite.mobile.data.local.DatabaseSeeder
import com.panalsuite.mobile.data.local.PanalSuiteDatabase
import com.panalsuite.mobile.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppContainer(private val database: PanalSuiteDatabase) {
    val authRepository: AuthRepository by lazy { AuthRepository(database.userDao()) }
}

class PanalSuiteApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: PanalSuiteDatabase by lazy { PanalSuiteDatabase.getInstance(this) }
    val container: AppContainer by lazy { AppContainer(database) }
    val authRepository: AuthRepository get() = container.authRepository

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            DatabaseSeeder(database).seedIfEmpty()
        }
    }
}
