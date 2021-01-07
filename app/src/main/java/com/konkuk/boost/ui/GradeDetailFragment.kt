package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.konkuk.boost.data.grade.ParcelableGrade
import com.konkuk.boost.databinding.FragmentGradeDetailBinding
import com.konkuk.boost.viewmodels.GradeDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GradeDetailFragment : Fragment() {

    private var _binding: FragmentGradeDetailBinding? = null
    private val binding get() = _binding!!
    val viewModel: GradeDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGradeDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGrade()
        binding.subjectNameTextView.isSelected = true
    }

    private fun setGrade() {
        val grd = requireArguments().getParcelable<ParcelableGrade>("grade")
        viewModel.setGrade(grd)
    }
}