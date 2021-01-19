package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.konkuk.boost.adapters.SyllabusAdapter
import com.konkuk.boost.data.course.LectureInfo
import com.konkuk.boost.databinding.FragmentCourseBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSyllabusRecyclerViewConfig()
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
                // 내비게이션 제어
            }
        }

        val searchView = binding.syllabusSearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
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

        viewModel.fetchAllSyllabus(2021, 1)
    }
}