package com.corgaxm.ku_alarmy.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.corgaxm.ku_alarmy.R
import com.corgaxm.ku_alarmy.databinding.FragmentHomeBinding
import com.corgaxm.ku_alarmy.persistence.GraduationSimulationEntity
import com.corgaxm.ku_alarmy.utils.DateTimeConverter
import com.corgaxm.ku_alarmy.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeToolbar()
        setGraduationSimulationRefreshButton()
        fetchGraduationSimulationFromLocalDb()
        fetchGraduationSimulationFromServer()
        observeLogout()
        observeGraduationSimulation()
        observeStdNo()
    }

    private fun observeStdNo() {
        viewModel.stdNo.observe(viewLifecycleOwner) {
            viewModel.fetchGraduationSimulationFromServer()
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
        viewModel.graduationSimulationData.observe(viewLifecycleOwner) {
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
            makeTableRow(tableLayout, "이수구분", "기준학점", "취득학점", "잔여학점")

            // 테이블 바디 생성
            for (simulation in simulations) {
                makeTableRow(
                    tableLayout,
                    simulation.classification,
                    simulation.standard,
                    simulation.acquired,
                    simulation.remainder
                )
            }
        }
    }

    private fun makeTableRow(
        tableLayout: TableLayout,
        first: String,
        second: String,
        third: String,
        fourth: String
    ) {
        val context = requireContext()
        val row = TableRow(context)
        val textList = listOf(first, second, third, fourth)

        for (text in textList) {
            val textView = TextView(context)
            textView.text = text
            textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            textView.typeface = Typeface.DEFAULT_BOLD
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            row.addView(textView)
        }

        tableLayout.addView(row)
    }

    private fun makeTableRow(
        tableLayout: TableLayout,
        classification: String,
        standardValue: Int,
        acquiredValue: Int,
        remainderValue: Int
    ) {
        val context = requireContext()
        val row = TableRow(context)
        val textList = listOf(
            classification,
            "$standardValue",
            "$acquiredValue",
            "$remainderValue"
        )

        for (text in textList) {
            val textView = TextView(context)
            textView.text = text
            textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            row.addView(textView)
        }

        tableLayout.addView(row)
    }
}