package com.konkuk.boost.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.konkuk.boost.adapters.SyllabusBookAdapter
import com.konkuk.boost.adapters.SyllabusWeekPlanAdapter
import com.konkuk.boost.adapters.SyllabusWorkAdapter
import com.konkuk.boost.databinding.FragmentCourseSummaryBinding
import com.konkuk.boost.viewmodels.CourseSummaryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseSummaryFragment : Fragment() {

    private var _binding: FragmentCourseSummaryBinding? = null
    private val binding get() = _binding!!
    private val courseSummaryViewModel: CourseSummaryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseSummaryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = courseSummaryViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLectureData()
        setBookRecyclerViewConfig()
        setWorkRecyclerViewConfig()
        setWeekPlanRecyclerViewConfig()
        setLikeListener()
        courseSummaryViewModel.fetchDetailSyllabus()
    }

    private fun setLikeListener() {
        // 토글기능 추가해야 함.
        binding.likeButton.setOnClickListener { _ ->
            // test
            courseSummaryViewModel.updateLikeCourse()
            val animator = ValueAnimator.ofFloat(0f, 0.5f).setDuration(1000L)
            animator.addUpdateListener {
                binding.likeButton.progress = it.animatedValue as Float
            }
            animator.start()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        courseSummaryViewModel.detailSyllabusResponse.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val bookAdapter = binding.bookRecyclerView.adapter as SyllabusBookAdapter
            bookAdapter.submitList(courseSummaryViewModel.getBookList())

            val workAdapter = binding.workRecyclerView.adapter as SyllabusWorkAdapter
            workAdapter.submitList(courseSummaryViewModel.getWorkList())

            val weekPlanAdapter = binding.weekRecyclerView.adapter as SyllabusWeekPlanAdapter
            weekPlanAdapter.submitList(courseSummaryViewModel.getWeekPlanList())
        }
    }

    private fun setLectureData() {
        val sbjtId = requireArguments().getString("subjectId") ?: return
        val sbjtName = requireArguments().getString("subjectName") ?: return
        val prof = requireArguments().getString("professor") ?: return
        val year = requireArguments().getInt("year")
        val semester = requireArguments().getInt("semester")
        courseSummaryViewModel.setSubjectId(sbjtId)
        courseSummaryViewModel.setSubjectName(sbjtName)
        courseSummaryViewModel.setProfessor(prof)
        courseSummaryViewModel.setYear(year)
        courseSummaryViewModel.setSemester(semester)
    }

    private fun setBookRecyclerViewConfig() {
        val context = requireContext()
        val recyclerView = binding.bookRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SyllabusBookAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setWorkRecyclerViewConfig() {
        val context = requireContext()
        val recyclerView = binding.workRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SyllabusWorkAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setWeekPlanRecyclerViewConfig() {
        val context = requireContext()
        val recyclerView = binding.weekRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = SyllabusWeekPlanAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.HORIZONTAL
            )
        )
    }
}