package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.konkuk.boost.R
import com.konkuk.boost.adapters.LikeCourseAdapter
import com.konkuk.boost.databinding.FragmentCourseBinding
import com.konkuk.boost.persistence.LikeCourseEntity
import com.konkuk.boost.viewmodels.CourseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseFragment : Fragment() {

    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CourseViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAllLikeCourses()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.allLikeCoursesResponse.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val allLikeCourses = it.data

            val adapter = binding.allLikeCoursesRecyclerView.adapter as LikeCourseAdapter
            adapter.submitList(allLikeCourses)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFabListener()
        setLikeCourseRecyclerViewConfig()
    }

    private fun setFabListener() {
        val context = requireContext()
        val fabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        val fabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close)

        binding.fab.setOnClickListener {
            if (viewModel.isFabOpened()) {
                binding.searchFab.apply {
                    startAnimation(fabClose)
                    isClickable = false
                }
                viewModel.setFabOpened(false)
            } else {
                binding.searchFab.apply {
                    startAnimation(fabOpen)
                    isClickable = true
                }
                viewModel.setFabOpened(true)
            }
        }

        binding.searchFab.setOnClickListener {
            viewModel.setFabOpened(false)
            findNavController().navigate(R.id.action_mainFragment_to_courseSearchFragment)
        }
    }

    private fun setLikeCourseRecyclerViewConfig() {
        val context = requireContext()
        val recyclerView = binding.allLikeCoursesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = LikeCourseAdapter()

        adapter.itemClickListener = object : LikeCourseAdapter.OnItemClickListener {
            override fun onItemClick(likeCourse: LikeCourseEntity) {
                val bundle = bundleOf(
                    "subjectId" to likeCourse.subjectId,
                    "subjectName" to likeCourse.subjectName,
                    "professor" to likeCourse.professor,
                    "year" to likeCourse.year,
                    "semester" to likeCourse.semester
                )
                findNavController().navigate(
                    R.id.action_mainFragment_to_courseSummaryFragment,
                    bundle
                )
            }
        }

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }
}