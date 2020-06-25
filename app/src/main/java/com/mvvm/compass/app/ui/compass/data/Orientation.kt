package com.mvvm.compass.app.ui.compass.data

data class Orientation(
    val compassAzumith: Float = 0f,
    val compassAzumithFix: Float = 0f,
    val destinationAzumith: Float = 0f,
    val destinationAzumithFix: Float = 0f,
    val isArrowVisible: Boolean = false
)