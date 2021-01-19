package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.konkuk.boost.databinding.FragmentCourseSummaryBinding
import com.konkuk.boost.viewmodels.CourseSummaryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseSummaryFragment : Fragment() {

    private var _binding: FragmentCourseSummaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CourseSummaryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseSummaryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLectureData()
        viewModel.fetchDetailSyllabus(2021, 1)
    }

    private fun setLectureData() {
        val sbjtId = requireArguments().getString("subjectId") ?: return
        val year = requireArguments().getInt("year")
        val semester = requireArguments().getInt("semester")
        viewModel.setSubjectId(sbjtId)
        viewModel.setYear(year)
        viewModel.setSemester(semester)
    }
}