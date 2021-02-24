package com.konkuk.boost.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.konkuk.boost.data.auth.TuitionSection
import com.konkuk.boost.databinding.FragmentRegistrationAndScholarshipBinding
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.RegistrationAndScholarshipViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegistrationAndScholarshipFragment : Fragment() {

    private var _binding: FragmentRegistrationAndScholarshipBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegistrationAndScholarshipViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationAndScholarshipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchTuitionResponse()
        setTuitionAndScholarshipRecyclerViewConfig()
    }

    private fun setTuitionAndScholarshipRecyclerViewConfig() {
        val context = requireContext()
        val recyclerView = binding.tuitionAndScholarshipRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SectionedRecyclerViewAdapter()
        adapter.addSection("tuition", TuitionSection())
//        adapter.addSection("scholarship", ScholarshipSection())
        recyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.tuitionResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    updateTuition()
                }
                UseCase.Status.ERROR -> {
                    Log.e("ku-boost", "${it.message}")
                }
            }
        }
    }

    private fun updateTuition() {
        val adapter =
            binding.tuitionAndScholarshipRecyclerView.adapter as SectionedRecyclerViewAdapter
        val section = adapter.getSection("tuition") as TuitionSection
        section.items = viewModel.getTuition()
        adapter.notifyDataSetChanged()
    }
}