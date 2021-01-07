package com.konkuk.boost.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.adapters.GradeAdapter
import com.konkuk.boost.data.grade.ParcelableGrade
import com.konkuk.boost.databinding.FragmentHomeBinding
import com.konkuk.boost.persistence.GradeEntity
import com.konkuk.boost.persistence.GraduationSimulationEntity
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.StorageUtils.checkStoragePermission
import com.konkuk.boost.viewmodels.HomeViewModel
import com.konkuk.boost.views.CaptureUtils.capture
import com.konkuk.boost.views.ChartUtils
import com.konkuk.boost.views.CustomTableRow
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val view = binding.root
        view.postDelayed({ view.requestLayout() }, 0)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeToolbar()
        setChartConfig()
        setClickListenerToTotalGradeDetailFragment()
        setCurrentGradesRecyclerViewConfig()
        setCardViewLongClickListener()
        fetchFromLocalDb()
        observeLogout()
        observeGraduationSimulation()
        observeStdNo()
        observeAllValidGrades()
        observeCurrentGrades()
        observeLoading()
        observeFetching()
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
                    R.id.action_homeFragment_to_gradeDetailFragment,
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
                }
            }
        }
    }

    private fun observeLoading() {
        viewModel.allGradesLoading.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    if (!viewModel.hasData()) {
                        // 로컬 DB가 비어있으면 다이얼로그를 띄움
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
                    } catch (exception: Exception) {
                        Log.e("yoonseop", "${exception.message}")
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
            true
        }

        binding.apply {
            currentCardView.setOnLongClickListener(cardClickListener)
            totalCardView.setOnLongClickListener(cardClickListener)
            simulationCardView.setOnLongClickListener(cardClickListener)
        }
    }

    private fun setClickListenerToTotalGradeDetailFragment() {
        binding.apply {
            readMoreButton.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_totalGradeDetailFragment)
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
    }

    private fun makeToolbar() {
        binding.apply {
            val typeface = Typeface.createFromAsset(requireActivity().assets, "vermin_vibes_slant.ttf")
            collapsingToolbarLayout.setExpandedTitleTypeface(typeface)
            collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT)
            toolbar.inflateMenu(R.menu.menu_main)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.moreIcon -> {
                        true
                    }
                    R.id.logout -> {
                        viewModel?.logout()
                        true
                    }
                    R.id.openSourceLicense -> {
                        startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                        OssLicensesMenuActivity.setActivityTitle(getString(R.string.prompt_opensource_title))
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun observeLogout() {
        viewModel.logoutResponse.observe(viewLifecycleOwner) {
            viewModel.clearLogoutResource()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    private fun observeGraduationSimulation() {
        viewModel.graduationSimulation.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val simulations: List<GraduationSimulationEntity>

            try {
                simulations = it.data
            } catch (exception: Exception) {
                return@observe
            }

            if (simulations.isEmpty())
                return@observe

            // 테이블 동적으로 생성
            val tableLayout = binding.graduationSimulationContentLayout
            tableLayout.removeAllViewsInLayout()

            // 테이블 헤더 생성
            val context = requireContext()
            CustomTableRow(context).attach(
                tableLayout,
                getString(R.string.prompt_classification),
                getString(R.string.prompt_standard_grade),
                getString(R.string.prompt_acquired_grade),
                getString(R.string.prompt_rest_grade)
            )

            // 테이블 바디 생성
            for (simulation in simulations) {
                simulation.apply {
                    CustomTableRow(context).attach(
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
            lineDataSet.valueFormatter = MyValueFormatter()
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

class MyValueFormatter : ValueFormatter() {
    private val mFormat = DecimalFormat("###,###,##0.00")
    override fun getFormattedValue(value: Float): String {
        return mFormat.format(value).toString()
    }
}