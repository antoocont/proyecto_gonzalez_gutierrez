package com.panalsuite.mobile

import android.app.Application

class PanalSuiteApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}