package com.mvvm.compass.app.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.compass.app.di.ViewModelFactory
import com.mvvm.compass.app.di.annotations.ViewModelKey
import com.mvvm.compass.app.ui.compass.CompassViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CompassViewModel::class)
    abstract fun bindCompassViewModels(compassViewModel: CompassViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}