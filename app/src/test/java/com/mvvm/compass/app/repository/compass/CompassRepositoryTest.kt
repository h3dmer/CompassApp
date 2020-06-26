package com.mvvm.compass.app.repository.compass

import android.hardware.SensorEvent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mvvm.compass.app.repository.compass.location.LocationDataSource
import com.mvvm.compass.app.repository.compass.orientation.OrientationDataSource
import com.mvvm.compass.app.ui.compass.data.GeoLocation
import com.mvvm.compass.app.ui.compass.data.Orientation
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class CompassRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var compassRepository: CompassRepository

    private val locationDataSource = mock(LocationDataSource::class.java)

    private val orientationDataSource = mock(OrientationDataSource::class.java)

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        compassRepository = CompassRepository(locationDataSource, orientationDataSource)
    }

    @Test
    fun test_get_current_location_success() {
        val data = GeoLocation(1.0, 1.0)

        `when`(locationDataSource.getCurrentLocation()).thenReturn(Flowable.just(
            data
        ))

        compassRepository.getCurrentLocation()

        verify(locationDataSource, times(1))
    }

    @Test
    fun test_when_sensorChanged_return_orientation() {

        val event = mock(SensorEvent::class.java)

        val result = Orientation(1f, 1f, 1f, 1f, false)

        `when`(compassRepository.sensorChanged(event)).thenReturn(
            Orientation(
                1f,
                1f,
                1f,
                1f,
                false
            )
        )

        Assert.assertEquals(result, compassRepository.sensorChanged(event))
    }

    @Test
    fun test_when_sensorChanged_return_wrong_value() {

        val event = mock(SensorEvent::class.java)

        val result = Orientation(2f, 2f, 2f, 2f, true)

        `when`(compassRepository.sensorChanged(event)).thenReturn(
            Orientation(
                2f,
                2f,
                2f,
                2f,
                false
            )
        )

        compassRepository.sensorChanged(event)

        Assert.assertNotEquals(result, compassRepository.sensorChanged(event))
    }

    @Test
    fun test_update_current_location() {
        val geoLocation = mock(GeoLocation::class.java)

        compassRepository.updateCurrentLocation(geoLocation)

        verify(geoLocation, times(1))
    }

    @Test
    fun test_update_destination_location() {
        val geoLocation = mock(GeoLocation::class.java)

        compassRepository.updateDestinationLocation(geoLocation)

        verify(geoLocation, times(1))
    }
}