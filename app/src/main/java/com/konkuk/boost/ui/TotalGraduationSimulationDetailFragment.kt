package com.konkuk.boost.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.data.grade.SubjectAreaSection
import com.konkuk.boost.databinding.FragmentTotalGraduationSimulationDetailBinding
import com.konkuk.boost.utils.MessageUtils
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.GraduationSimulationViewModel
import com.konkuk.boost.views.TableRowUtils
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TotalGraduationSimulationDetailFragment : Fragment() {

    private var _binding: FragmentTotalGraduationSimulationDetailBinding? = null
    private val binding get() = _binding!!
    val viewModel: GraduationSimulationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentTotalGraduationSimulationDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchGraduationSimulation()
        setElectiveRecyclerViewConfig()
        fetchElectiveStatus()
    }

    private fun setElectiveRecyclerViewConfig() {
        val context = requireContext()
        val recyclerView = binding.electiveRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SectionedRecyclerViewAdapter()
        adapter.addSection("basic", SubjectAreaSection(1))
        adapter.addSection("core", SubjectAreaSection(viewModel.getCoreFlag()))
        recyclerView.adapter = adapter
    }

    private fun fetchElectiveStatus() {
        viewModel.fetchElectiveStatus()
    }

    private fun fetchGraduationSimulation() {
        viewModel.fetchGraduationSimulationFromLocalDb()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeGraduationSimulation()
        observeSubjectAreaCounts()
    }

    private fun observeSubjectAreaCounts() {
        viewModel.subjectAreaCounts.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    updateSubjectAreaStatus()
                }
                UseCase.Status.ERROR -> {
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeGraduationSimulation() {
        viewModel.graduationSimulation.observe(viewLifecycleOwner) {
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
                getString(R.string.prompt_rest_grade),
                12,
                24
            )

            // 테이블 바디 생성
            for (simulation in simulations) {
                simulation.apply {
                    val row = TableRowUtils.attach(
                        context,
                        tableLayout,
                        classification,
                        standard,
                        acquired,
                        remainder,
                        12,
                        24
                    )

                    row.setOnClickListener {
                        if (acquired > 0) {
                            val bundle = bundleOf("classification" to classification)
                            findNavController().navigate(
                                R.id.action_totalGraduationSimulationDetailFragment_to_graduationSimulationDetailFragment,
                                bundle
                            )
                        }
                    }

                    val outValue = TypedValue()
                    context.theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
                    row.setBackgroundResource(outValue.resourceId)
                }
            }
        }
    }

    private fun updateSubjectAreaStatus() {
        try {
            val areaWithCounts = viewModel.getAreaWithCounts()
            val adapter =
                binding.electiveRecyclerView.adapter as SectionedRecyclerViewAdapter

            val basicList = areaWithCounts.filter { item -> item.area.type == 1 }
            val coreList = areaWithCounts.filter { item -> item.area.type == 2 }

            val basicSection = adapter.getSection("basic") as SubjectAreaSection
            val coreSection = adapter.getSection("core") as SubjectAreaSection

            basicSection.itemList = basicList
            coreSection.itemList = coreList

            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e(MessageUtils.LOG_KEY, "${e.message}")
        }
    }

}