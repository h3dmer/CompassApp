package com.mvvm.compass.app.di.modules

import com.mvvm.compass.app.di.annotations.ActivityScope
import com.mvvm.compass.app.ui.mainactivity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun contributeMainActivity(): MainActivity

}