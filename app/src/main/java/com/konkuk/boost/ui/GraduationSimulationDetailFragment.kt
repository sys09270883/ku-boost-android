package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.konkuk.boost.databinding.FragmentGraduationSimulationDetailBinding
import com.konkuk.boost.persistence.grade.GradeContract
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.utils.StorageUtils
import com.konkuk.boost.viewmodels.GraduationSimulationDetailViewModel
import com.konkuk.boost.views.CaptureUtils
import com.konkuk.boost.views.ChartUtils
import com.konkuk.boost.views.DialogUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class GraduationSimulationDetailFragment : Fragment() {

    private var _binding: FragmentGraduationSimulationDetailBinding? = null
    private val binding get() = _binding!!
    val viewModel: GraduationSimulationDetailViewModel by viewModel()
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
        _binding = FragmentGraduationSimulationDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClassification()
        setChartConfig()
        setCardViewLongClickListener()
    }

    private fun setCardViewLongClickListener() {
        val activity = requireActivity()
        binding.cardView.setOnLongClickListener {
            val builder = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.question_grades))
                .setPositiveButton(getString(R.string.prompt_yes)) { _, _ ->
                    if (StorageUtils.checkStoragePermission(activity)) {
                        CaptureUtils.capture(activity, it)
                        Snackbar.make(
                            binding.container,
                            getString(R.string.prompt_save),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                .setNegativeButton(getString(R.string.prompt_no)) { _, _ ->
                }
            val dialog = DialogUtils.recolor(builder.create())
            dialog.show()
            true
        }
    }

    private fun setClassification() {
        val clf = requireArguments().getString("classification") ?: return
        viewModel.setClassification(clf)
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isFetched())
            return

        viewModel.fetchGradesByClassification()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = requireContext()

        viewModel.gradesByClassification.observe(viewLifecycleOwner) {
            if (it.data == null)
                return@observe

            val gradesByClassification = it.data
            val validGradesByClassification =
                it.data.filter { grade -> grade.type == GradeContract.Type.VALID.value }

            val (avr, _) = GradeUtils.totalAverages(validGradesByClassification)
            // 전체평점
            ChartUtils.makeGradeChart(
                binding.totalPieChart,
                getString(R.string.prompt_total),
                avr,
                colors.first(),
                colors.last()
            )

            // 성적분포
            val characterGradesMap = GradeUtils.characterGrades(validGradesByClassification)
            val characterGrades = mutableListOf<PieEntry>()

            for (grade in characterGradesMap) {
                characterGrades.add(PieEntry(grade.value, grade.key))
            }

            ChartUtils.makeSummaryChart(binding.summaryPieChart, colors, characterGrades)

            // 리사이클러뷰
            val recyclerView = binding.gradeRecyclerview
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = GradeAdapter()
            adapter.submitList(gradesByClassification.toMutableList())
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
                        R.id.action_graduationSimulationDetailFragment_to_gradeDetailFragment,
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

    private fun setChartConfig() {
        ChartUtils.setGradeConfigWith(binding.totalPieChart, getString(R.string.prompt_total), true)
        ChartUtils.setSummaryConfig(binding.summaryPieChart, true)
    }
}