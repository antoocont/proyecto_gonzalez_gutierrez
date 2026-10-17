package com.panalsuite.mobile

import android.app.Application
import com.panalsuite.mobile.data.local.DatabaseSeeder
import com.panalsuite.mobile.data.local.PanalSuiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PanalSuiteApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: PanalSuiteDatabase by lazy { PanalSuiteDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            DatabaseSeeder(database).seedIfEmpty()
        }
    }
}