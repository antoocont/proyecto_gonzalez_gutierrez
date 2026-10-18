package com.panalsuite.mobile

import android.content.Context
import com.panalsuite.mobile.data.local.DatabaseSeeder
import com.panalsuite.mobile.data.local.PanalSuiteDatabase
import com.panalsuite.mobile.data.local.SessionManager
import com.panalsuite.mobile.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

class AppContainer(context: Context) {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val database = PanalSuiteDatabase.getInstance(context)
    private val seedJob = applicationScope.async { DatabaseSeeder(database).seedIfEmpty() }

    val sessionManager = SessionManager(context)

    val authRepository = AuthRepository(
        userDao = database.userDao(),
        sessionManager = sessionManager,
        awaitDatabaseReady = { seedJob.await() }
    )
}