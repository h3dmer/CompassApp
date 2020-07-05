package com.mvvm.compass.app.ui.compass

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.mvvm.compass.app.repository.compass.CompassRepository
import com.mvvm.compass.app.ui.compass.data.GeoLocation
import com.mvvm.compass.app.ui.compass.data.Orientation
import com.mvvm.compass.app.utils.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class CompassViewModel @Inject constructor(
    application: Application,
    private val compassRepository: CompassRepository
) : AndroidViewModel(application), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var compositeDisposable = CompositeDisposable()

    private var _orientation = MutableLiveData<Orientation>()
    var orientation: LiveData<Orientation> = _orientation

    private val _setDestinationLatitude = MutableLiveData<Event<String>>()
    val setDestinationLatitude: LiveData<Event<String>> get() = _setDestinationLatitude

    private val _setDestinationLongitude = MutableLiveData<Event<String>>()
    val setDestinationLongitude: LiveData<Event<String>> get() = _setDestinationLongitude


    val currentLocation = LiveDataReactiveStreams.fromPublisher(compassRepository.getCurrentLocation())
    val destinationLatitude = MutableLiveData<Double>()
    val destinationLongitude = MutableLiveData<Double>()

    init {
        registerSensor()
        destinationLatitude.value = 0.0
        destinationLongitude.value = 0.0
    }

    private fun registerSensor() {
        sensorManager =
            getApplication<Application>().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun unregisterSensor() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        compassRepository.sensorChanged(event)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _orientation.value = it },
                { Timber.d(it) }
            ).addTo(compositeDisposable)
    }

    fun initDialog(dialogTitle: String, option: Int) {
        when (option) {
            LATITUDE_DIALOG -> {
                _setDestinationLatitude.value = Event(dialogTitle)
            }
            LONGITUDE_DIALOG -> {
                _setDestinationLongitude.value = Event(dialogTitle)
            }
        }
    }

    fun setDestinationLatitude(latitude: Double) {
        destinationLatitude.value = latitude
        destinationLongitude.value?.let { longitude ->
            compassRepository.updateDestinationLocation(GeoLocation(latitude, longitude))
        }
    }

    fun setDestinationLongitude(longitude: Double) {
        destinationLongitude.value = longitude
        destinationLatitude.value?.let { latitude ->
            compassRepository.updateDestinationLocation(GeoLocation(latitude, longitude))
        }
    }

    override fun onCleared() {
        super.onCleared()
        unregisterSensor()
        compositeDisposable.clear()
    }

    companion object {
        const val LATITUDE_DIALOG = 1
        const val LONGITUDE_DIALOG = 2
    }
}