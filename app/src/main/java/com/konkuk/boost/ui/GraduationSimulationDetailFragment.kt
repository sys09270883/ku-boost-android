package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieEntry
import com.konkuk.boost.R
import com.konkuk.boost.adapters.GradeAdapter
import com.konkuk.boost.data.grade.ParcelableGrade
import com.konkuk.boost.databinding.FragmentGraduationSimulationDetailBinding
import com.konkuk.boost.persistence.grade.GradeContract
import com.konkuk.boost.persistence.grade.GradeEntity
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.viewmodels.GraduationSimulationDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GraduationSimulationDetailFragment : Fragment() {

    private var _binding: FragmentGraduationSimulationDetailBinding? = null
    private val binding get() = _binding!!
    val viewModel: GraduationSimulationDetailViewModel by viewModel()

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

            // Draw total average pie chart.
            binding.totalPieChart.makeChart(getString(R.string.prompt_total), avr)

            // Draw grade distribution pie chart.
            val characterGradesMap = GradeUtils.characterGrades(validGradesByClassification)
            val characterGrades = mutableListOf<PieEntry>()

            for (grade in characterGradesMap) {
                characterGrades.add(PieEntry(grade.value, grade.key))
            }

            binding.summaryPieChart.makeChart(characterGrades)

            // Update grade recycler view.
            val recyclerView = binding.gradeRecyclerview
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = GradeAdapter()
            adapter.submitList(gradesByClassification.toMutableList())
            adapter.itemClickListener = object : GradeAdapter.OnItemClickListener {
                override fun onItemClick(gradeEntity: GradeEntity) {
                    // Navigate with grade entity.
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

}