package com.mvvm.compass.app.repository.compass.location

import com.mvvm.compass.app.ui.compass.data.GeoLocation
import io.reactivex.Flowable

interface LocationDataSource {

    fun getCurrentLocation(): Flowable<GeoLocation>
}