package com.mvvm.compass.app.repository.compass.orientation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.mvvm.compass.app.ui.compass.data.GeoLocation
import com.mvvm.compass.app.ui.compass.data.Orientation
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Singleton
class OrientationDataSourceImpl @Inject constructor() : OrientationDataSource {

    private val alpha = 0.97f

    private var orientation = Orientation()

    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)

    private var azimuth = 0f
    private var azimuthFix = 0f

    private var destinationAzimuth = 0f
    private var destinationAzimuthFix = 0f

    private var currentLocation = GeoLocation(0.0, 0.0)
    private var destination = GeoLocation(0.0, 0.0)

    override fun sensorChanged(event: SensorEvent?): Observable<Orientation> {
        return calculateCompassRotation(event)
    }

    private fun calculateCompassRotation(event: SensorEvent?): Observable<Orientation> {
        calculateSensorValues(event)

        val rotation = FloatArray(9)
        val success = SensorManager.getRotationMatrix(
            rotation,
            null,
            mGravity,
            mGeomagnetic
        )
        if (success) {
            calculateCompassRotationAzimuth(rotation)
            if (destination.latitude > 0 || destination.longitude > 0) {
                calculateBearingValues()
            } else { orientation.isArrowVisible = false }
        }

        return Observable.just(orientation)
    }

    private fun calculateSensorValues(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0]
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1]
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2]
            }
        }
    }

    private fun calculateCompassRotationAzimuth(rotation: FloatArray) {
        val orientationArray = FloatArray(3)
        SensorManager.getOrientation(rotation, orientationArray)
        azimuth = Math.toDegrees(orientationArray[0].toDouble()).toFloat()
        azimuth = azimuth.plus(360).rem(360)
        orientation.compassAzimuth = azimuth
        orientation.compassAzimuthFix = azimuthFix
        azimuthFix = azimuth
    }

    private fun calculateBearingValues() {
        orientation.isArrowVisible = true
        currentLocation.let { currentLocation ->
            destinationAzimuth = azimuth.minus(
                bearing(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    destination.latitude,
                    destination.longitude
                )
            ).toFloat()
            orientation.destinationAzimuth = destinationAzimuth
            orientation.destinationAzimuthFix = destinationAzimuthFix
            destinationAzimuthFix = destinationAzimuth
        }
    }

    override fun updateCurrentLocation(geoLocation: GeoLocation) {
        currentLocation = geoLocation
    }

    override fun updateDestinationLocation(geoLocation: GeoLocation) {
        destination = geoLocation
    }

    private fun bearing(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Double {
        val latitude1 = Math.toRadians(startLat)
        val latitude2 = Math.toRadians(endLat)
        val longDiff = Math.toRadians(endLng - startLng)
        val y = sin(longDiff) * cos(latitude2)
        val x = cos(latitude1) * sin(latitude2) - sin(latitude1) * cos(latitude2) * cos(longDiff)
        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }
}