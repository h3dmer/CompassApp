package com.mvvm.compass.app.ui.compass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.mvvm.compass.app.databinding.FragmentCompassBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CompassFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val compassViewModel by viewModels<CompassViewModel> { viewModelProvider }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCompassBinding.inflate(inflater, container, false).apply {
            viewModel = compassViewModel
            lifecycleOwner = this@CompassFragment
        }
        return binding.root
    }
}