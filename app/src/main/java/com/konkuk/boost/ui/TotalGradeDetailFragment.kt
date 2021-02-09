package com.konkuk.boost.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.adapters.GradeAdapter
import com.konkuk.boost.data.grade.ParcelableGrade
import com.konkuk.boost.databinding.FragmentTotalGradeDetailBinding
import com.konkuk.boost.persistence.GradeEntity
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.StorageUtils.checkStoragePermission
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.TotalGradeViewModel
import com.konkuk.boost.views.CaptureUtils.capture
import com.konkuk.boost.views.ChartUtils
import com.konkuk.boost.views.DialogUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class TotalGradeDetailFragment : Fragment() {

    private var _binding: FragmentTotalGradeDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TotalGradeViewModel by viewModel()
    private val colors: List<Int> by lazy {
        val context = requireContext()
        listOf(
            ContextCompat.getColor(context, R.color.pastelRed),
            ContextCompat.getColor(context, R.color.pastelOrange),
            ContextCompat.getColor(context, R.color.pastelYellow),
            ContextCompat.getColor(context, R.color.pastelGreen),
            ContextCompat.getColor(context, R.color.pastelBlue),
            ContextCompat.getColor(context, R.color.pastelIndigo),
            ContextCompat.getColor(context, R.color.pastelPurple),
            ContextCompat.getColor(context, R.color.pastelDeepPurple),
            ContextCompat.getColor(context, R.color.pastelBrown),
            ContextCompat.getColor(context, R.color.pastelLightGray),
        )
    }

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
        setChartConfig()
        setCardViewLongClickListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeAllValidGrades()
        observeRankOfSelectedSemester()
    }

    @SuppressLint("SetTextI18n")
    private fun observeRankOfSelectedSemester() {
        viewModel.selectedRankResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    val dept = viewModel.getDept()
                    val (rank, total) = viewModel.getRankAndTotal()

                    binding.rankTextView.apply {
                        text = "$dept ${total}명 중 ${rank}등"
                    }
                }
                UseCase.Status.ERROR -> {
                }
            }
        }
    }

    private fun setCardViewLongClickListener() {
        val activity = requireActivity()
        binding.cardView.setOnLongClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(getString(R.string.app_name))
            builder.setMessage(getString(R.string.question_grades))
            builder.setPositiveButton(getString(R.string.prompt_yes)) { _, _ ->
                if (checkStoragePermission(activity)) {
                    capture(activity, it)
                    Snackbar.make(
                        binding.container,
                        getString(R.string.prompt_save),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            builder.setNegativeButton(getString(R.string.prompt_no)) { _, _ ->
            }
            val dialog = DialogUtils.recolor(builder.create())
            dialog.show()
            true
        }
    }

    private fun setChartConfig() {
        ChartUtils.setGradeConfigWith(binding.totalPieChart, getString(R.string.prompt_total), true)
        ChartUtils.setGradeConfigWith(binding.majorPieChart, getString(R.string.prompt_major), true)
        ChartUtils.setSummaryConfig(binding.summaryPieChart, true)
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

            val semesterArray = Array(yearAndSemesters.size) { "" }
            for (i in yearAndSemesters.indices) {
                semesterArray[i] =
                    "${yearAndSemesters[i].first}년도 ${GradeUtils.translate(yearAndSemesters[i].second)}학기"
            }

            val spinner = binding.semesterSpinner
            spinner.adapter = ArrayAdapter(
                context, R.layout.spinner_item, semesterArray
            )
            spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val (year, semester) = yearAndSemesters[spinner.selectedItemPosition]

                    Log.d("yoonseop", "year: $year semester: ${GradeUtils.translate(semester)}")

                    // 1학기, 2학기에만 등수 표기
                    if (semester == 1 || semester == 3) {
                        binding.rankTextView.visibility = View.VISIBLE
                        viewModel.fetchSelectedRankFromLocalDb(
                            year,
                            GradeUtils.translate(semester).toInt()
                        )
                    } else {
                        // 그 외 학기에는 등수 표기하지 않음
                        binding.rankTextView.visibility = View.GONE
                    }

                    val selectedGrades = mutableListOf<GradeEntity>()
                    for (data in it.data) {
                        if (year == data.year && semester == data.semester) {
                            selectedGrades.add(data)
                        }
                    }

                    val (avr, majorAvr) = GradeUtils.totalAverages(selectedGrades)
                    // 전체평점
                    ChartUtils.makeGradeChart(
                        binding.totalPieChart,
                        getString(R.string.prompt_total),
                        avr,
                        colors.first(),
                        colors.last()
                    )

                    // 전공평점
                    ChartUtils.makeGradeChart(
                        binding.majorPieChart,
                        getString(R.string.prompt_major),
                        majorAvr,
                        colors.first(),
                        colors.last()
                    )

                    // 성적분포
                    val characterGradesMap = GradeUtils.characterGrades(selectedGrades)
                    val characterGrades = mutableListOf<PieEntry>()

                    for (grade in characterGradesMap) {
                        characterGrades.add(PieEntry(grade.value, grade.key))
                    }

                    ChartUtils.makeSummaryChart(binding.summaryPieChart, colors, characterGrades)

                    // 학기 업데이트
                    val adapter = binding.gradeRecyclerview.adapter as GradeAdapter
                    adapter.submitList(selectedGrades)

                    // 색상 업데이트
                    (parent?.getChildAt(0) as? TextView)?.setTextColor(Color.WHITE)
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
                    val grade = ParcelableGrade(
                        evaluationMethod = gradeEntity.evaluationMethod,
                        year = gradeEntity.year,
                        semester = GradeUtils.translate(gradeEntity.semester),
                        classification = gradeEntity.classification,
                        characterGrade = gradeEntity.characterGrade,
                        grade = gradeEntity.grade,
                        professor = gradeEntity.professor,
                        subjectId = gradeEntity.subjectId,
                        subjectName = gradeEntity.subjectName,
                        subjectNumber = gradeEntity.subjectNumber,
                        subjectPoint = gradeEntity.subjectPoint
                    )
                    val bundle = bundleOf("grade" to grade)
                    findNavController().navigate(
                        R.id.action_totalGradeDetailFragment_to_gradeDetailFragment,
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

    override fun onResume() {
        super.onResume()
        fetchAllGradesFromLocal()
        binding.semesterSpinner.apply {
            setSelection(viewModel.getSelectedPosition())
            (getChildAt(0) as? TextView)?.setTextColor(Color.WHITE)
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.setSelectedPosition(binding.semesterSpinner.selectedItemPosition)
    }
}