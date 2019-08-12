package com.loneoaktech.test.firebasedemo

import android.app.Application
import timber.log.Timber

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant( Timber.DebugTree() )
    }
}