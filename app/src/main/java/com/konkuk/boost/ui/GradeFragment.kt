package com.konkuk.boost.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.*
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.adapters.GradeAdapter
import com.konkuk.boost.data.grade.ParcelableGrade
import com.konkuk.boost.databinding.FragmentGradeBinding
import com.konkuk.boost.persistence.GradeEntity
import com.konkuk.boost.persistence.GraduationSimulationEntity
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.StorageUtils.checkStoragePermission
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.GradeViewModel
import com.konkuk.boost.views.CaptureUtils.capture
import com.konkuk.boost.views.ChartUtils
import com.konkuk.boost.views.CustomValueFormatter
import com.konkuk.boost.views.DialogUtils
import com.konkuk.boost.views.TableRowUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class GradeFragment : Fragment() {

    private var _binding: FragmentGradeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GradeViewModel by viewModel()
    private var dialog: AlertDialog? = null
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
    private val cardMessages: HashMap<Int, String> by lazy {
        hashMapOf(
            R.id.currentCardView to getString(R.string.question_current_grades),
            R.id.totalCardView to getString(R.string.question_total_grades),
            R.id.simulationCardView to getString(R.string.question_graduation_simulation),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGradeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val view = binding.root
        view.postDelayed({ view.requestLayout() }, 0)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChartConfig()
        setReadMoreClickListener()
        setCurrentGradesRecyclerViewConfig()
        setCardViewLongClickListener()
        fetchFromLocalDb()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLogout()
        observeGraduationSimulation()
        observeStdNo()
        observeAllValidGrades()
        observeCurrentGrades()
        observeLoading()
        observeFetching()
        observeRankInserted()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalRank() {
        val dept = viewModel.getDept()
        val (rank, total) = viewModel.getRankAndTotal()

        binding.rankTextView.apply {
            text = "$dept ${total}명 중 ${rank}등"
        }
    }

    private fun observeRankInserted() {
        viewModel.totalRankResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    viewModel.fetchTotalRankFromLocalDb()
                    updateTotalRank()
                }
                UseCase.Status.ERROR -> {
                }
            }
        }
    }

    private fun setCurrentGradesRecyclerViewConfig() {
        val recyclerView = binding.currentGradeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = GradeAdapter()
        adapter.itemClickListener = object : GradeAdapter.OnItemClickListener {
            override fun onItemClick(gradeEntity: GradeEntity) {
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
                    R.id.action_mainFragment_to_gradeDetailFragment,
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

    private fun observeFetching() {
        viewModel.fetched.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    viewModel.fetchGraduationSimulationFromLocalDb()
                    viewModel.fetchCurrentGradesFromLocalDb()
                    viewModel.fetchTotalGradesFromLocalDb()
                    viewModel.fetchTotalRankFromLocalDb()
                }
            }
        }
    }

    private fun observeLoading() {
        viewModel.allGradesLoading.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    if (!viewModel.hasData()) {
                        // 첫 로그인 시 로컬 데이터베이스가 비어있는 경우 다이얼로그를 띄움
                        val context = requireContext()
                        val builder = AlertDialog.Builder(context)
                        dialog = builder
                            .setTitle(getString(R.string.app_name))
                            .setMessage(getString(R.string.prompt_chart_no_data))
                            .setProgressBar(ProgressBar(context))
                            .setCancelable(false)
                            .create()
                        dialog?.show()
                    }
                }
                false -> {
                    try {
                        if (dialog?.isShowing == true) dialog?.dismiss()
                    } catch (e: Exception) {
                        Log.e("ku-boost", "${e.message}")
                    }
                }
            }
        }
    }

    private fun observeCurrentGrades() {
        viewModel.currentGrades.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val currentGrades = it.data

            // 파이 차트
            val (avr, majorAvr) = GradeUtils.totalAverages(currentGrades)

            // 전체평점
            ChartUtils.makeGradeChart(
                binding.currentTotalPieChart,
                getString(R.string.prompt_total),
                avr,
                colors.first(),
                colors.last()
            )

            // 전공평점
            ChartUtils.makeGradeChart(
                binding.currentMajorPieChart,
                getString(R.string.prompt_major),
                majorAvr,
                colors.first(),
                colors.last()
            )

            // 성적분포
            val characterGradesMap = GradeUtils.characterGrades(currentGrades)
            val characterGrades = mutableListOf<PieEntry>()

            for (grade in characterGradesMap) {
                characterGrades.add(PieEntry(grade.value, grade.key))
            }

            ChartUtils.makeSummaryChart(binding.currentSummaryPieChart, colors, characterGrades)

            // 금학기 성적
            val recyclerView = binding.currentGradeRecyclerView
            val adapter = recyclerView.adapter as GradeAdapter
            adapter.submitList(currentGrades.toMutableList())
        }
    }

    private fun setCardViewLongClickListener() {
        val activity = requireActivity()
        val cardClickListener = View.OnLongClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(getString(R.string.app_name))
            builder.setMessage(cardMessages[it.id])
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

        binding.apply {
            currentCardView.setOnLongClickListener(cardClickListener)
            totalCardView.setOnLongClickListener(cardClickListener)
            simulationCardView.setOnLongClickListener(cardClickListener)
        }
    }

    private fun setReadMoreClickListener() {
        binding.apply {
            readGradeMoreButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_totalGradeDetailFragment)
            }

            readSimulationMoreButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_totalGraduationSimulationDetailFragment)
            }
        }
    }

    private fun setChartConfig() {
        ChartUtils.setGradeConfigWith(binding.currentTotalPieChart, "전체")
        ChartUtils.setGradeConfigWith(binding.currentMajorPieChart, "전공")
        ChartUtils.setSummaryConfig(binding.currentSummaryPieChart)
        ChartUtils.setGradeConfigWith(binding.totalPieChart, "전체")
        ChartUtils.setGradeConfigWith(binding.majorPieChart, "전공")
        ChartUtils.setSummaryConfig(binding.summaryPieChart)
        ChartUtils.setLineChartConfig(binding.totalLineChart)
    }

    private fun observeStdNo() {
        viewModel.stdNo.observe(viewLifecycleOwner) {
            if (viewModel.isFetched())
                return@observe

            viewModel.fetchGraduationSimulationFromServer()
            viewModel.fetchAllGradesFromServer()
            viewModel.makeTotalRank()
        }
    }

    private fun fetchFromLocalDb() {
        if (viewModel.isFetched())
            return
        if (!viewModel.hasData())
            return

        viewModel.fetchGraduationSimulationFromLocalDb()
        viewModel.fetchCurrentGradesFromLocalDb()
        viewModel.fetchTotalGradesFromLocalDb()
        viewModel.fetchTotalRankFromLocalDb()
    }

    private fun observeLogout() {
        viewModel.logoutResponse.observe(viewLifecycleOwner) {
            viewModel.clearLogoutResource()
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }

    private fun observeGraduationSimulation() {
        viewModel.graduationSimulation.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val simulations: List<GraduationSimulationEntity>

            try {
                simulations = it.data
            } catch (e: Exception) {
                return@observe
            }

            if (simulations.isEmpty())
                return@observe

            // 테이블 동적으로 생성
            val tableLayout = binding.graduationSimulationContentLayout
            tableLayout.removeAllViewsInLayout()

            // 테이블 헤더 생성
            val context = requireContext()
            TableRowUtils.attach(
                context,
                tableLayout,
                getString(R.string.prompt_classification),
                getString(R.string.prompt_standard_grade),
                getString(R.string.prompt_acquired_grade),
                getString(R.string.prompt_rest_grade)
            )

            // 테이블 바디 생성
            for (simulation in simulations) {
                simulation.apply {
                    TableRowUtils.attach(
                        context,
                        tableLayout,
                        classification,
                        standard,
                        acquired,
                        remainder
                    )
                }
            }
        }
    }

    private fun observeAllValidGrades() {
        viewModel.allValidGrades.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val context = requireContext()
            val allGrades = it.data
            val averages = GradeUtils.average(allGrades)

            // 2. 평점 파이 차트 그리기
            // 2-1. 전체 평점
            val (avr, majorAvr) = GradeUtils.totalAverages(allGrades)

            ChartUtils.makeGradeChart(
                binding.totalPieChart,
                "전체",
                avr,
                colors.first(),
                colors.last()
            )

            // 2-2. 전공 평점
            ChartUtils.makeGradeChart(
                binding.majorPieChart,
                "전공",
                majorAvr,
                ContextCompat.getColor(context, R.color.pastelRed),
                ContextCompat.getColor(context, R.color.pastelLightGray)
            )

            // 3. 성적 분포 파이 차트 그리기
            val characterGradesMap = GradeUtils.characterGrades(allGrades)
            val characterGrades = mutableListOf<PieEntry>()

            for (grade in characterGradesMap) {
                characterGrades.add(PieEntry(grade.value, grade.key))
            }

            ChartUtils.makeSummaryChart(binding.summaryPieChart, colors, characterGrades)

            // 4. 라인 차트 그리기
            val lineChart = binding.totalLineChart
            lineChart.clear()

            val dataSet = mutableListOf<Entry>()

            for (i in averages.indices) {
                dataSet += Entry(i.toFloat(), averages[i].toFloat())
            }

            val lineDataSet = LineDataSet(dataSet, null)
            lineDataSet.color =
                ContextCompat.getColor(context, R.color.pastelRed)
            lineDataSet.setCircleColor(R.color.pastelRed)
            lineDataSet.circleHoleRadius = 4f
            lineDataSet.circleRadius = 6f
            lineDataSet.setDrawCircleHole(true)
            lineDataSet.lineWidth = 2f
            lineDataSet.valueFormatter = CustomValueFormatter()
            val lineData = LineData()
            lineData.addDataSet(lineDataSet)
            lineData.setValueTextColor(
                ContextCompat.getColor(
                    context, R.color.secondaryTextColor
                )
            )
            lineData.setValueTextSize(12f)
            lineChart.data = lineData
        }
    }

    private fun AlertDialog.Builder.setProgressBar(progressBar: ProgressBar): AlertDialog.Builder {
        val activity = requireActivity()
        val container = FrameLayout(activity)
        container.addView(progressBar)
        val containerParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val marginHorizontal = 48f
        val margin = 48f
        containerParams.leftMargin = marginHorizontal.toInt()
        containerParams.rightMargin = marginHorizontal.toInt()
        containerParams.bottomMargin = margin.toInt()
        container.layoutParams = containerParams
        val superContainer = FrameLayout(requireContext())
        superContainer.addView(container)
        setView(superContainer)
        return this
    }

}