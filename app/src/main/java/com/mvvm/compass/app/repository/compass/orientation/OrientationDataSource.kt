package com.mvvm.compass.app.repository.compass.orientation

import android.hardware.SensorEvent
import com.mvvm.compass.app.ui.compass.data.GeoLocation
import com.mvvm.compass.app.ui.compass.data.Orientation
import io.reactivex.Observable

interface OrientationDataSource {

    fun sensorChanged(event: SensorEvent?): Observable<Orientation>

    fun updateCurrentLocation(geoLocation: GeoLocation)

    fun updateDestinationLocation(geoLocation: GeoLocation)

}