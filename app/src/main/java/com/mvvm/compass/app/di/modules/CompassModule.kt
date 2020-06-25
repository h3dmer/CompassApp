package com.mvvm.compass.app.di.modules

import android.content.Context
import com.mvvm.compass.app.repository.compass.location.LocationDataSourceImpl
import com.mvvm.compass.app.repository.compass.orientation.OrientationDataSourceImpl
import com.patloew.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CompassModule {

    @Provides
    @Singleton
    fun provideRxLocation(context: Context): RxLocation {
        return RxLocation(context)
    }

    @Provides
    @Singleton
    fun provideCompassLocationSource(rxLocation: RxLocation): LocationDataSourceImpl {
        return LocationDataSourceImpl(rxLocation)
    }

    @Provides
    @Singleton
    fun provideCompassOrientationSource(): OrientationDataSourceImpl {
        return OrientationDataSourceImpl()
    }
}
