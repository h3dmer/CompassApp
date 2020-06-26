package com.mvvm.compass.app.ui.compass.data

data class Orientation(
    var destinationAzimuth: Float = 0f,
    var destinationAzimuthFix: Float = 0f,
    var compassAzimuth: Float = 0f,
    var compassAzimuthFix: Float = 0f,
    var isArrowVisible: Boolean = false
)