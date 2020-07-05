package com.mvvm.compass.app.repository.compass

import android.hardware.SensorEvent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mvvm.compass.app.repository.compass.location.LocationDataSource
import com.mvvm.compass.app.repository.compass.orientation.OrientationDataSource
import com.mvvm.compass.app.ui.compass.data.GeoLocation
import com.mvvm.compass.app.ui.compass.data.Orientation
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompassRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var compassRepository: CompassRepository

    private val locationDataSource: LocationDataSource = mock()

    private val orientationDataSource: OrientationDataSource = mock()


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        compassRepository = CompassRepository(locationDataSource, orientationDataSource)
    }

    @After
    fun validate() {
        validateMockitoUsage()
    }

    @Test
    fun test_get_current_location_success() {
        val data = GeoLocation(1.0, 1.0)
        reset(locationDataSource)

        whenever(locationDataSource.getCurrentLocation()).thenReturn(
            Flowable.just(
                data
            )
        )

        compassRepository.getCurrentLocation().test().assertValue(data).dispose()
        verify(locationDataSource).getCurrentLocation()
    }

    @Test
    fun test_when_sensorChanged_return_orientation() {
        reset(orientationDataSource)
        val event = mock<SensorEvent>()

        val result = Orientation(1f, 1f, 1f, 1f, false)

        whenever(orientationDataSource.sensorChanged(event)).thenReturn(
            Observable.just(
                result
            )
        )

        whenever(
            compassRepository.sensorChanged(event)
        ).thenReturn(
            Observable.just(result)
        )

        compassRepository.sensorChanged(event).test().assertValue(result).dispose()

        verify(orientationDataSource, times(1)).sensorChanged(event)
    }

    @Test
    fun test_when_sensorChanged_return_wrong_value() {
        val event = mock<SensorEvent>()
        val result = Observable.just(Orientation(2f, 2f, 2f, 2f, true))
        val wrongValue = Observable.just(
            Orientation(
                2f,
                2f,
                2f,
                2f,
                false
            )
        )

        whenever(orientationDataSource.sensorChanged(event)).thenReturn(
            result
        )

        whenever(
            compassRepository.sensorChanged(event)
        ).thenReturn(
            result
        )

        compassRepository.sensorChanged(event)
            .test()
            .assertValue { result != wrongValue }
            .dispose()
        verify(orientationDataSource, times(1)).sensorChanged(event)
    }

    @Test
    fun test_update_current_location() {
        val geoLocation = GeoLocation(1.0, 1.0)

        compassRepository.updateCurrentLocation(geoLocation)

        verify(orientationDataSource, times(1)).updateCurrentLocation(geoLocation)
    }

    @Test
    fun test_update_destination_location() {
        val geoLocation = GeoLocation(1.0, 1.0)

        compassRepository.updateDestinationLocation(geoLocation)

        verify(orientationDataSource, times(1)).updateDestinationLocation(geoLocation)
    }
}