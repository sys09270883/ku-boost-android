package com.corgaxm.ku_alarmy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.corgaxm.ku_alarmy.R
import com.corgaxm.ku_alarmy.adapters.GradeAdapter
import com.corgaxm.ku_alarmy.databinding.FragmentTotalGradeDetailBinding
import com.corgaxm.ku_alarmy.persistence.GradeEntity
import com.corgaxm.ku_alarmy.utils.GradeUtils
import com.corgaxm.ku_alarmy.viewmodels.TotalGradeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TotalGradeDetailFragment : Fragment() {

    private var _binding: FragmentTotalGradeDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TotalGradeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalGradeDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAllValidGrades()
    }

    private fun fetchAllGradesFromLocal() {
        if (viewModel.isFetched())
            return

        viewModel.fetchAllGradesFromLocal()
    }

    private fun observeAllValidGrades() {
        viewModel.allValidGrades.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val context = requireContext()
            val yearAndSemesters = GradeUtils.semesters(it.data)
            val translate = hashMapOf(1 to "1", 2 to "하계계절", 3 to "2", 4 to "동계계절")
            val reverseTranslate = hashMapOf("1학기" to 1, "하계계절학기" to 2, "2학기" to 3, "동계계절학기" to 4)

            val semesterArray = Array(yearAndSemesters.size) { "" }
            for (i in yearAndSemesters.indices) {
                semesterArray[i] =
                    "${yearAndSemesters[i].first}년도 ${translate[yearAndSemesters[i].second]}학기"
            }

            val spinner = binding.semesterSpinner
            spinner.adapter = ArrayAdapter(
                context, R.layout.support_simple_spinner_dropdown_item, semesterArray
            )
            spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = spinner.selectedItem.toString()
                    val split = selected.split("년도", " ")
                        .filter { item -> item.isNotBlank() }
                    val year = split[0].toInt()
                    val semester = reverseTranslate[split[1]]!!

                    val selectedItems = mutableListOf<GradeEntity>()
                    for (data in it.data) {
                        if (year == data.year && semester == data.semester) {
                            selectedItems.add(data)
                        }
                    }

                    val adapter = binding.gradeRecyclerview.adapter as GradeAdapter
                    adapter.submitList(selectedItems)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }
            }

            // 리사이클러 뷰 어댑터
            val recyclerView = binding.gradeRecyclerview
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = GradeAdapter()
            adapter.itemClickListener = object : GradeAdapter.OnItemClickListener {
                override fun onItemClick(gradeEntity: GradeEntity) {
                    // GradeEntity 정보를 가지고 fragment 전환
                    // 데이터를 어떻게 가져갈지?
                    findNavController().navigate(R.id.action_totalGradeDetailFragment_to_gradeDetailFragment)
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

    override fun onResume() {
        super.onResume()
        fetchAllGradesFromLocal()
        binding.semesterSpinner.setSelection(viewModel.getSelectedPosition())
    }

    override fun onPause() {
        super.onPause()
        viewModel.setSelectedPosition(binding.semesterSpinner.selectedItemPosition)
    }
}