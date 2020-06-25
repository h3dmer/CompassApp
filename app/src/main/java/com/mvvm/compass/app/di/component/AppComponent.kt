package com.mvvm.compass.app.di.component

import android.app.Application
import com.mvvm.compass.app.di.modules.ActivityModule
import com.mvvm.compass.app.di.modules.AppModule
import com.mvvm.compass.app.di.modules.CompassModule
import com.mvvm.compass.app.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    CompassModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}