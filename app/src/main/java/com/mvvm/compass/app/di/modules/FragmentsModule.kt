package com.mvvm.compass.app.di.modules

import com.mvvm.compass.app.di.annotations.FragmentScope
import com.mvvm.compass.app.ui.compass.CompassFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCompassFragment(): CompassFragment
}