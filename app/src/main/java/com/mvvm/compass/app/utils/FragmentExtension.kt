package com.mvvm.compass.app.utils

import android.text.InputType
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input

fun Fragment.geolocationDialog(title: String, doNext: (Double?) -> Unit) {

    MaterialDialog(requireContext()).show {
        val inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        input(hint = title, inputType = inputType) { _, text ->
            doNext.invoke(text.toString().toDoubleOrNull())
        }
        positiveButton()
    }
}
