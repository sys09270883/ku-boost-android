package com.konkuk.boost.ui

import android.os.Bundle
import android.util.Log
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseSearchFragment : Fragment() {

    private var _binding: FragmentCourseSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CourseSearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
        viewModel.syllabusResponse.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            if (it.data.lectureInfoList.isEmpty()) {
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
                    viewModel.setSemester(spinner.selectedItemPosition + 1)
                    viewModel.fetchAllSyllabus()
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
        viewModel.setYear(DateTimeConverter.currentYear())
        viewModel.setSemester(viewModel.getSemester())
    }

    override fun onResume() {
        super.onResume()
        fetchAllSyllabus()
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
                    "year" to viewModel.getYear(), "semester" to viewModel.getSemester()
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

                adapter.submitList(viewModel.getFilteredList(newText).toMutableList())
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
        if (viewModel.isSyllabusFetched())
            return

        viewModel.fetchAllSyllabus()
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