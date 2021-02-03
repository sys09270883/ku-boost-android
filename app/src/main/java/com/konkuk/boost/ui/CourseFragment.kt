package com.konkuk.boost.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.boost.R
import com.konkuk.boost.adapters.LikeCourseAdapter
import com.konkuk.boost.data.course.RegistrationStatusData
import com.konkuk.boost.databinding.FragmentCourseBinding
import com.konkuk.boost.viewmodels.CourseViewModel
import com.skydoves.balloon.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.timer

class CourseFragment : Fragment() {

    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CourseViewModel by viewModel()
    private var timer: Timer? = null
    private val infoBalloon: Balloon by lazy {
        val context = requireContext()
        createBalloon(context) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setPadding(16)
            setArrowPosition(0.3f)
            setArrowOrientation(ArrowOrientation.TOP)
            setCornerRadius(4f)
            setTextGravity(Gravity.START)
            setTextSize(14f)
            setTextColor(ContextCompat.getColor(context, R.color.infoTextColor))
            setAlpha(0.9f)
            setBackgroundColor(ContextCompat.getColor(context, R.color.infoBackground))
            setText(getString(R.string.course_info_with_emoji))
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.courseAndRegistrationStatus.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe

            val adapter = binding.allLikeCoursesRecyclerView.adapter as LikeCourseAdapter
            adapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        updateCourseInfo()
        timer = timer(period = 3000L) {
            updateCourseInfo()
        }
    }

    private fun updateCourseInfo() {
        viewModel.fetchSelectedSemester()
        viewModel.fetchAllLikeCourses()
        viewModel.fetchRegistrationStatus()
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFabListener()
        setLikeCourseRecyclerViewConfig()
        setInfoConfig()
    }

    private fun setInfoConfig() {
        binding.infoTextView.setOnClickListener {
            when (infoBalloon.isShowing) {
                true -> infoBalloon.dismiss()
                false -> infoBalloon.showAlignBottom(it)
            }
        }
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
            override fun onItemClick(registrationStatusData: RegistrationStatusData) {
                val bundle = bundleOf(
                    "subjectId" to registrationStatusData.likeCourseEntity.subjectId,
                    "subjectName" to registrationStatusData.likeCourseEntity.subjectName,
                    "professor" to registrationStatusData.likeCourseEntity.professor,
                    "year" to registrationStatusData.likeCourseEntity.year,
                    "semester" to registrationStatusData.likeCourseEntity.semester
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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (viewModel.isFabOpened()) {
                        binding.searchFab.apply {
                            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fab_close))
                            isClickable = false
                        }
                        viewModel.setFabOpened(false)
                    }
                    binding.fab.hide()
                } else if (dy < 0) {
                    binding.fab.show()
                }
            }
        })
    }
}