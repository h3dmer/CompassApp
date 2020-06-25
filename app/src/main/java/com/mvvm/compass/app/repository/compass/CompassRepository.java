package com.mvvm.compass.app.repository.compass;

import android.hardware.SensorEvent;

import com.mvvm.compass.app.repository.compass.location.LocationDataSource;
import com.mvvm.compass.app.repository.compass.location.LocationDataSourceImpl;
import com.mvvm.compass.app.repository.compass.orientation.OrientationDataSource;
import com.mvvm.compass.app.repository.compass.orientation.OrientationDataSourceImpl;
import com.mvvm.compass.app.ui.compass.data.GeoLocation;
import com.mvvm.compass.app.ui.compass.data.Orientation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class CompassRepository implements LocationDataSource, OrientationDataSource {

    private LocationDataSourceImpl mLocationDataSource;
    private OrientationDataSourceImpl mOrientationDataSource;

    @Inject
    public CompassRepository(LocationDataSourceImpl mLocationDataSource, OrientationDataSourceImpl mOrientationDataSource) {
        this.mLocationDataSource = mLocationDataSource;
        this.mOrientationDataSource = mOrientationDataSource;
    }

    @NotNull
    @Override
    public Flowable<GeoLocation> getCurrentLocation() {
        return mLocationDataSource.getCurrentLocation()
            .flatMap(Flowable::just);
    }

    @NotNull
    @Override
    public Orientation sensorChanged(@Nullable SensorEvent event) {
        return mOrientationDataSource.sensorChanged(event);
    }

    @Override
    public void updateCurrentLocation(@NotNull GeoLocation geoLocation) {
        mOrientationDataSource.updateCurrentLocation(geoLocation);
    }

    @Override
    public void updateDestinationLocation(@NotNull GeoLocation geoLocation) {
        mOrientationDataSource.updateDestinationLocation(geoLocation);
    }
}
