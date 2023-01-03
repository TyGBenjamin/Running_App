package com.androiddevs.runningappyt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Custom default [Application].
 *
 * @constructor Create instance of [BaseApplication]
 */
@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
