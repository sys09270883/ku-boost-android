package com.konkuk.boost.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.*
import com.konkuk.boost.R
import com.konkuk.boost.adapters.GradeAdapter
import com.konkuk.boost.data.grade.ParcelableGrade
import com.konkuk.boost.databinding.FragmentGradeBinding
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.MessageUtils
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.GradeViewModel
import com.konkuk.boost.viewmodels.MainFragmentViewModel
import com.konkuk.boost.views.TableRowUtils
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class GradeFragment : Fragment() {

    private var _binding: FragmentGradeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainFragmentViewModel by lazy { requireParentFragment().getViewModel() }
    private val gradeViewModel: GradeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGradeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.gradeViewModel = gradeViewModel
        binding.mainViewModel = mainViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setReadMoreClickListener()
        setCurrentGradesRecyclerViewConfig()
        fetchFromLocalDb()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLogout()
        observeGraduationSimulation()
        observeAllValidGrades()
        observeCurrentGrades()
        observeFetching()
        observeRankInserted()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalRank() {
        val dept = gradeViewModel.getDept()
        val (rank, total) = gradeViewModel.getRankAndTotal()

        binding.rankTextView.apply {
            text = "$dept ${total}명 중 ${rank}등"
        }
    }

    private fun observeRankInserted() {
        gradeViewModel.totalRankResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    gradeViewModel.fetchTotalRankFromLocalDb()
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
        mainViewModel.fetched.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    if (gradeViewModel.isFetched()) {
                        return@observe
                    }

                    Log.d(MessageUtils.LOG_KEY, "Received from MainFragmentViewModel: FETCHED.")
                    gradeViewModel.fetchGraduationSimulationFromLocalDb()
                    Log.d(MessageUtils.LOG_KEY, "Update graduation simulation from local DB.")
                    gradeViewModel.fetchCurrentGradesFromLocalDb()
                    Log.d(MessageUtils.LOG_KEY, "Update current grades from local DB.")
                    gradeViewModel.fetchTotalGradesFromLocalDb()
                    Log.d(MessageUtils.LOG_KEY, "Update total grades from local DB.")
                    gradeViewModel.fetchTotalRankFromLocalDb()
                    Log.d(MessageUtils.LOG_KEY, "Update total rank from local DB.")
                    gradeViewModel.setFetch(true)
                }
                false -> {
                    Log.d(MessageUtils.LOG_KEY, "Received from MainFragmentViewModel: NOT FETCHED.")
                }
            }
        }
    }

    private fun observeCurrentGrades() {
        gradeViewModel.currentGrades.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val currentGrades = it.data

            val (avr, majorAvr) = GradeUtils.totalAverages(currentGrades)

            // Draw current pie chart.
            binding.currentTotalPieChart.makeChart(getString(R.string.prompt_total), avr)

            // Draw current major pie chart.
            binding.currentMajorPieChart.makeChart(getString(R.string.prompt_major), majorAvr)

            // Draw current grade distribution pie chart.
            val characterGradesMap = GradeUtils.characterGrades(currentGrades)
            val characterGrades = mutableListOf<PieEntry>()

            for (grade in characterGradesMap) {
                characterGrades.add(PieEntry(grade.value, grade.key))
            }

            binding.currentSummaryPieChart.makeChart(characterGrades)

            // Update current semester recycler view.
            val recyclerView = binding.currentGradeRecyclerView
            val adapter = recyclerView.adapter as GradeAdapter
            adapter.submitList(currentGrades.toMutableList())
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

    private fun fetchFromLocalDb() {
        if (!gradeViewModel.hasData())
            return

        gradeViewModel.fetchGraduationSimulationFromLocalDb()
        gradeViewModel.fetchCurrentGradesFromLocalDb()
        gradeViewModel.fetchTotalGradesFromLocalDb()
        gradeViewModel.fetchTotalRankFromLocalDb()
    }

    private fun observeLogout() {
        gradeViewModel.logoutResponse.observe(viewLifecycleOwner) {
            gradeViewModel.clearLogoutResource()
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }

    private fun observeGraduationSimulation() {
        gradeViewModel.graduationSimulation.observe(viewLifecycleOwner) {
            val simulations = it.data ?: return@observe

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
        gradeViewModel.allValidGrades.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val allGrades = it.data
            val averages = GradeUtils.average(allGrades)

            val (avr, majorAvr) = GradeUtils.totalAverages(allGrades)

            // Draw total grade pie chart.
            binding.totalPieChart.makeChart(getString(R.string.prompt_total), avr)

            // Draw total major grade pie chart.
            binding.majorPieChart.makeChart(getString(R.string.prompt_major), majorAvr)

            // Draw grade distribution pie chart.
            val characterGradesMap = GradeUtils.characterGrades(allGrades)
            val characterGrades = mutableListOf<PieEntry>()

            for (grade in characterGradesMap) {
                characterGrades.add(PieEntry(grade.value, grade.key))
            }

            binding.summaryPieChart.makeChart(characterGrades)

            // Draw grade line chart.
            val entities = mutableListOf<Entry>()

            for (i in averages.indices) {
                entities += Entry(i.toFloat(), averages[i].toFloat())
            }

            binding.totalLineChart.makeChart(entities)
        }
    }

}