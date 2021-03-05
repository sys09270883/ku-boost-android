package com.konkuk.boost.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.konkuk.boost.data.auth.DeptTransferSection
import com.konkuk.boost.data.auth.StudentStateChangeSection
import com.konkuk.boost.databinding.FragmentAcademicEventBinding
import com.konkuk.boost.utils.MessageUtils
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.AcademicEventViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class AcademicEventFragment : Fragment() {

    private var _binding: FragmentAcademicEventBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AcademicEventViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAcademicEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAcademicEventRecyclerViewConfig()
        viewModel.fetchDeptTransferInfo()
        viewModel.fetchStudentStateChangeInfo()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.deptTransferResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    updateDeptTransfer()
                }
                UseCase.Status.ERROR -> Log.e(MessageUtils.LOG_KEY, "${it.message}")
            }
        }

        viewModel.studentStateChangeResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    updateStudentStateChange()
                }
                UseCase.Status.ERROR -> Log.e(MessageUtils.LOG_KEY, "${it.message}")
            }
        }
    }

    private fun updateStudentStateChange() {
        val adapter = binding.academicEventRecyclerView.adapter as SectionedRecyclerViewAdapter
        val section = adapter.getSection("stdstate") as StudentStateChangeSection
        section.items = viewModel.getStudentStateChangeInfo()
        adapter.notifyDataSetChanged()
    }

    private fun updateDeptTransfer() {
        val adapter = binding.academicEventRecyclerView.adapter as SectionedRecyclerViewAdapter
        val section = adapter.getSection("dept") as DeptTransferSection
        section.items = viewModel.getDeptTransferInfo()
        adapter.notifyDataSetChanged()
    }

    private fun setAcademicEventRecyclerViewConfig() {
        val context = requireContext()
        val recyclerView = binding.academicEventRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SectionedRecyclerViewAdapter()
        adapter.addSection("dept", DeptTransferSection())
        adapter.addSection("stdstate", StudentStateChangeSection())
        recyclerView.adapter = adapter
    }
}