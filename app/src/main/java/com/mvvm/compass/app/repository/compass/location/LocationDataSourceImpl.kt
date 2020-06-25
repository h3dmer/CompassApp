package com.mvvm.compass.app.repository.compass.location

import android.annotation.SuppressLint
import com.google.android.gms.location.LocationRequest
import com.mvvm.compass.app.ui.compass.data.GeoLocation
import com.patloew.rxlocation.RxLocation
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataSourceImpl @Inject constructor(private val rxLocation: RxLocation): LocationDataSource {

    private
    val customLocationRequest: LocationRequest
        get() {
            val locationRequest = LocationRequest()
            locationRequest.interval = 5000
            locationRequest.fastestInterval = 5000
            locationRequest.smallestDisplacement = 10f
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            return locationRequest
        }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flowable<GeoLocation> {
        return rxLocation.location().updates(customLocationRequest)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { position -> Observable.just(GeoLocation(position.latitude, position.longitude)) }
            .toFlowable(BackpressureStrategy.LATEST)
    }
}