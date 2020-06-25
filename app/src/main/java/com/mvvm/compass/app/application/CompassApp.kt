package com.mvvm.compass.app.application

import com.mvvm.compass.app.BuildConfig
import com.mvvm.compass.app.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

@Suppress("unused")
class CompassApp: DaggerApplication() {

    private val appComponent = DaggerAppComponent.factory().create(this)

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        appComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}