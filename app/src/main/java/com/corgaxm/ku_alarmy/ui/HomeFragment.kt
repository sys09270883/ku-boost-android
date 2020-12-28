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
import com.corgaxm.ku_alarmy.data.db.GraduationSimulationData
import com.corgaxm.ku_alarmy.databinding.FragmentHomeBinding
import com.corgaxm.ku_alarmy.utils.DateTimeConverter
import com.corgaxm.ku_alarmy.utils.GradeConverter
import com.corgaxm.ku_alarmy.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Integer.max

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
        observeLogout()
        fetchGraduationSimulationFromLocalDb()
        fetchGraduationSimulationFromServer()
        observeGraduationSimulation()
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

            val standard: GraduationSimulationData
            val acquired: GraduationSimulationData

            try {
                standard = it.data.first { item -> item.type == "standard" }
                acquired = it.data.first { item -> item.type == "acquired" }
            } catch (exception: NoSuchElementException) {
                return@observe
            }

            // 최종 업데이트 시간 바인딩
            binding.lastModifiedTimeTextView.text = DateTimeConverter.convert(standard.modifiedAt)

            // 테이블 동적으로 생성
            val tableLayout = binding.graduationSimulationContentLayout
            tableLayout.removeAllViewsInLayout()

            // 테이블 헤더 생성
            makeTableRow(tableLayout, "이수구분", "기준학점", "취득학점", "잔여학점")

            // 테이블 바디 생성
            if (standard.basicElective != null && acquired.basicElective != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("basicElective"),
                    standardValue = standard.basicElective,
                    acquiredValue = acquired.basicElective
                )
            }

            if (standard.advancedElective != null && acquired.advancedElective != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("advancedElective"),
                    standardValue = standard.advancedElective,
                    acquiredValue = acquired.advancedElective
                )
            }

            if (standard.generalElective != null && acquired.generalElective != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("generalElective"),
                    standardValue = standard.generalElective,
                    acquiredValue = acquired.generalElective
                )
            }

            if (standard.coreElective != null && acquired.coreElective != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("coreElective"),
                    standardValue = standard.coreElective,
                    acquiredValue = acquired.coreElective
                )
            }

            if (standard.normalElective != null && acquired.normalElective != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("normalElective"),
                    standardValue = standard.normalElective,
                    acquiredValue = acquired.normalElective
                )
            }

            if (standard.generalRequirement != null && acquired.generalRequirement != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("generalRequirement"),
                    standardValue = standard.generalRequirement,
                    acquiredValue = acquired.generalRequirement
                )
            }

            if (standard.majorRequirement != null && acquired.majorRequirement != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("majorRequirement"),
                    standardValue = standard.majorRequirement,
                    acquiredValue = acquired.majorRequirement
                )
            }

            if (standard.majorElective != null && acquired.majorElective != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("majorElective"),
                    standardValue = standard.majorElective,
                    acquiredValue = acquired.majorElective
                )
            }

            if (standard.dualElective != null && acquired.dualElective != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("dualElective"),
                    standardValue = standard.dualElective,
                    acquiredValue = acquired.dualElective
                )
            }

            if (standard.dualRequirement != null && acquired.dualRequirement != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("dualRequirement"),
                    standardValue = standard.dualRequirement,
                    acquiredValue = acquired.dualRequirement
                )
            }

            if (standard.dualMajorElective != null && acquired.dualMajorElective != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("dualMajorElective"),
                    standardValue = standard.dualMajorElective,
                    acquiredValue = acquired.dualMajorElective
                )
            }

            if (standard.etc != null && acquired.etc != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("etc"),
                    standardValue = standard.etc,
                    acquiredValue = acquired.etc
                )
            }

            if (standard.total != null && acquired.total != null) {
                makeTableRow(
                    tableLayout = tableLayout,
                    classification = GradeConverter.convert("total"),
                    standardValue = standard.total,
                    acquiredValue = acquired.total,
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
        acquiredValue: Int
    ) {
        val context = requireContext()
        val row = TableRow(context)
        val textList = listOf(
            classification,
            "$standardValue",
            "$acquiredValue",
            "${max(0, standardValue - acquiredValue)}"
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