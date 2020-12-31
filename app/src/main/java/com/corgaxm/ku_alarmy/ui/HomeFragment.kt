package com.corgaxm.ku_alarmy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.corgaxm.ku_alarmy.R
import com.corgaxm.ku_alarmy.databinding.FragmentHomeBinding
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationEntity
import com.corgaxm.ku_alarmy.utils.DateTimeConverter
import com.corgaxm.ku_alarmy.utils.GradeUtils
import com.corgaxm.ku_alarmy.viewmodels.HomeViewModel
import com.corgaxm.ku_alarmy.views.CustomTableRow
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeToolbar()
        setGraduationSimulationRefreshButton()
        setAllGradesRefreshButton()
        setChartConfig()
        fetchGraduationSimulationFromLocalDb()
        fetchGraduationSimulationFromServer()
        fetchAllGradesFromServer()
        observeLogout()
        observeGraduationSimulation()
        observeStdNo()
        observeAllValidGrades()
    }

    private fun setChartConfig() {
        binding.totalLineChart.apply {
            setNoDataText(getString(R.string.prompt_chart_no_data))
            animateXY(1000, 1000)
            description = null
            isDragEnabled = false
            setScaleEnabled(false)
            setDrawGridBackground(false)
            isHighlightPerDragEnabled = false
            setTouchEnabled(false)
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            legend.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(false)
        }

        binding.summaryPieChart.apply {
            setNoDataText(getString(R.string.prompt_chart_no_data))
            animateXY(1000, 1000)
            description = null
            setTouchEnabled(false)
            setDrawSlicesUnderHole(false)
            holeRadius = 0f
            legend.isEnabled = false
            isDrawHoleEnabled = false
            setEntryLabelColor(ContextCompat.getColor(context, R.color.primaryTextColor))
        }
    }

    private fun setAllGradesRefreshButton() {
        binding.allGradesRefreshButton.setOnClickListener {
            viewModel.fetchAllGradesFromServer()
        }
    }

    private fun fetchAllGradesFromServer() {
        viewModel.fetchAllGradesFromServer()
    }

    private fun observeStdNo() {
        viewModel.stdNo.observe(viewLifecycleOwner) {
            viewModel.fetchGraduationSimulationFromServer()
            viewModel.fetchAllGradesFromServer()
        }
    }

    private fun fetchGraduationSimulationFromServer() {
        viewModel.fetchGraduationSimulationFromServer()
    }

    private fun fetchGraduationSimulationFromLocalDb() {
        viewModel.fetchGraduationSimulationFromLocalDb()
    }

    private fun setGraduationSimulationRefreshButton() {
        binding.graduationSimulationRefreshButton.setOnClickListener {
            viewModel.fetchGraduationSimulationFromServer()
        }
    }

    private fun makeToolbar() {
        binding.apply {
//            toolbar.title = getString(R.string.app_name)
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

            // 최종 업데이트 시간 바인딩
            binding.lastModifiedTimeTextView.text =
                DateTimeConverter.convert(simulations[0].modifiedAt)

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

            // 1. 가져온 시간 표시
            binding.allGradesLastModifiedTimeTextView.text =
                DateTimeConverter.convert(allGrades[0].modifiedAt)

            // 2. 라인 차트 그리기
            val lineChart = binding.totalLineChart
            lineChart.clear()

            val dataSet = mutableListOf<Entry>()

            for (i in averages.indices) {
                dataSet += Entry(i.toFloat(), averages[i].toFloat())
            }

            val lineDataSet = LineDataSet(dataSet, null)
            lineDataSet.color =
                ContextCompat.getColor(context, R.color.secondaryColor)
            lineDataSet.setCircleColor(R.color.secondaryDarkColor)
            lineDataSet.lineWidth = 4f

            val lineData = LineData()
            lineData.addDataSet(lineDataSet)
            lineData.setValueTextColor(
                ContextCompat.getColor(
                    context, R.color.secondaryTextColor
                )
            )
            lineData.setValueTextSize(12f)
            lineChart.data = lineData

            // 3. 평점 파이 차트 그리기
            val pieChart = binding.summaryPieChart
            pieChart.clear()

            val characterGradesMap = GradeUtils.characterGrades(allGrades)
            val characterGrades = mutableListOf<PieEntry>()

            for (grade in characterGradesMap) {
                characterGrades.add(PieEntry(grade.value, grade.key))
            }

            val colors = listOf(
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

            val pieDataSet = PieDataSet(characterGrades, null)
            pieDataSet.colors = colors
            pieDataSet.setDrawValues(false)

            val pieData = PieData(pieDataSet)
            pieChart.data = pieData
        }
    }

}