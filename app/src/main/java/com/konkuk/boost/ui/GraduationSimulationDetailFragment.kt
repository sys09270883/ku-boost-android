package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.konkuk.boost.databinding.FragmentGraduationSimulationDetailBinding
import com.konkuk.boost.viewmodels.GraduationSimulationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GraduationSimulationDetailFragment : Fragment() {

    private var _binding: FragmentGraduationSimulationDetailBinding? = null
    private val binding get() = _binding!!
    val viewModel: GraduationSimulationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraduationSimulationDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

}