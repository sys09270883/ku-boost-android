package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.adapters.SyllabusAdapter
import com.konkuk.boost.data.course.LectureInfo
import com.konkuk.boost.databinding.FragmentCourseSearchBinding
import com.konkuk.boost.utils.DateTimeConverter
import com.konkuk.boost.viewmodels.CourseSearchViewModel
import com.konkuk.boost.viewmodels.CourseViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseSearchFragment : Fragment() {

    private var _binding: FragmentCourseSearchBinding? = null
    private val binding get() = _binding!!
    private val courseSearchViewModel: CourseSearchViewModel by viewModel()
    private val courseViewModel: CourseViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.courseViewModel = courseViewModel
        binding.courseSearchViewModel = courseSearchViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSyllabusRecyclerViewConfig()
        setYearAndSemester()
        setYearAndSemesterListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        courseViewModel.syllabusResponse.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            if (it.data.lectureInfoList.isEmpty()) {
                val adapter = binding.syllabusRecyclerView.adapter as SyllabusAdapter
                adapter.submitList(mutableListOf())
                Snackbar.make(
                    binding.container,
                    getString(R.string.prompt_no_syllabus),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setYearAndSemesterListener() {
        val activity = requireActivity()
        binding.dateTextView.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            val spinner = Spinner(activity)
            spinner.adapter = ArrayAdapter(
                activity, R.layout.spinner_item,
                arrayOf("1학기", "하계 계절학기", "2학기", "동계 계절학기")
            )
            builder.setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.prompt_choose_semester))
                .setSpinner(spinner)
                .setPositiveButton(getString(R.string.prompt_yes)) { _, _ ->
                    courseSearchViewModel.setSemester(spinner.selectedItemPosition + 1)
                    fetchAllSyllabus()
                }.setNegativeButton(getString(R.string.prompt_no)) { _, _ ->
                }
            val dlg = builder.create()
            dlg.setOnShowListener {
                dlg.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(activity, R.color.primaryTextColor)
                )
                dlg.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                    ContextCompat.getColor(activity, R.color.primaryTextColor)
                )
            }
            dlg.show()
        }
    }

    private fun setYearAndSemester() {
        courseSearchViewModel.setYear(DateTimeConverter.currentYear())
        courseSearchViewModel.setSemester(courseSearchViewModel.getSemester())
    }

    override fun onResume() {
        super.onResume()
        if (!courseViewModel.isSyllabusFetched()) {
            fetchAllSyllabus()
        }
    }

    private fun setSyllabusRecyclerViewConfig() {
        val context = requireContext()
        val recyclerView = binding.syllabusRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = SyllabusAdapter()
        adapter.itemClickListener = object : SyllabusAdapter.OnItemClickListener {
            override fun onItemClick(lectureInfo: LectureInfo) {
                val bundle = bundleOf(
                    "subjectId" to lectureInfo.subjectId,
                    "year" to courseSearchViewModel.getYear(),
                    "semester" to courseSearchViewModel.getSemester()
                )
                findNavController().navigate(
                    R.id.action_courseSearchFragment_to_courseSummaryFragment,
                    bundle
                )
            }
        }

        val searchView = binding.syllabusSearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty())
                    return false

                val list = courseViewModel.getSyllabusList()
                adapter.submitList(
                    courseSearchViewModel.getFilteredList(list, newText).toMutableList()
                )
                return true
            }
        })

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun fetchAllSyllabus() {
        courseViewModel.fetchAllSyllabus(
            courseSearchViewModel.getYear(),
            courseSearchViewModel.getSemester()
        )
    }

    private fun AlertDialog.Builder.setSpinner(spinner: Spinner): AlertDialog.Builder {
        val activity = requireActivity()
        val container = FrameLayout(activity)
        container.addView(spinner)
        val containerParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val marginHorizontal = 200f
        val marginTop = 16f
        containerParams.topMargin = (marginTop / 2).toInt()
        containerParams.leftMargin = marginHorizontal.toInt()
        containerParams.rightMargin = marginHorizontal.toInt()
        container.layoutParams = containerParams
        val superContainer = FrameLayout(requireContext())
        superContainer.addView(container)
        setView(superContainer)
        return this
    }

}