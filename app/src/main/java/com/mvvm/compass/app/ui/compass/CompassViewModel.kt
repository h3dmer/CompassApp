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
import com.mvvm.compass.app.ui.compass.data.Orientation
import javax.inject.Inject

class CompassViewModel @Inject constructor(
    application: Application,
    private val compassRepository: CompassRepository
) : AndroidViewModel(application), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private var _orientation = MutableLiveData<Orientation>()
    var orientation: LiveData<Orientation> = _orientation

    val currentLocation =
        LiveDataReactiveStreams.fromPublisher(compassRepository.getCurrentLocation())

    init {
        registerSensor()
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
        _orientation.value = compassRepository.sensorChanged(event)
    }

    override fun onCleared() {
        super.onCleared()
        unregisterSensor()
    }
}