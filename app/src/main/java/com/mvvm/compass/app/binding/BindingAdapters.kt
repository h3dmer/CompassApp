package com.mvvm.compass.app.binding

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView


@BindingAdapter("app:geoValue", "app:locationCoords")
fun setGeolocationText(target: MaterialTextView, value: Double, locationCoords: String) {
    target.text = String.format(locationCoords, value.toString())
}

@BindingAdapter("app:animateCompass", "app:currentAzimuth")
fun animateCompass(target: ImageView, azimuth: Float, currentAzimuth: Float) {
    val compassAnimation = RotateAnimation(
        currentAzimuth.unaryMinus(),
        azimuth.unaryMinus(),
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    ).apply {
        duration = 300
        repeatCount = 0
        fillAfter = true
    }

    target.startAnimation(compassAnimation)
}