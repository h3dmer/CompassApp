package com.mvvm.compass.app.ui.start

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mvvm.compass.app.R

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!appHasPermissions()) requestPermission()
        else compassFragmentNavigator()
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            //If permission is not granted
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) requestPermission()
            else compassFragmentNavigator()
        }
    }

    private fun compassFragmentNavigator() {
        findNavController().navigate(StartFragmentDirections.actionStartFragmentToCompassFragment())
    }

    private fun appHasPermissions(): Boolean {
        return checkFinePermission() && checkCoarsePermission()
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), 1)
    }

    private fun checkFinePermission(): Boolean {
        val result = checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCoarsePermission(): Boolean {
        val result = checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return result == PackageManager.PERMISSION_GRANTED
    }
}