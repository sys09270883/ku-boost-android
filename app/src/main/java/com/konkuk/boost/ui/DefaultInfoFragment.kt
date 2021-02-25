package com.konkuk.boost.ui

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.konkuk.boost.databinding.FragmentDefaultInfoBinding
import com.konkuk.boost.persistence.personal.PersonalInfoEntity
import com.konkuk.boost.utils.GradeUtils
import com.konkuk.boost.viewmodels.InfoViewModel
import com.konkuk.boost.views.PriorityRule
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.*

class DefaultInfoFragment : Fragment() {

    private var _binding: FragmentDefaultInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InfoViewModel by lazy { requireParentFragment().getViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDefaultInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = requireContext()

        viewModel.personalInfoResponse.observe(viewLifecycleOwner) {
            val flexBoxLayout = binding.flexBoxLayout

            val pq = PriorityQueue<Pair<PersonalInfoEntity, Int>>(
                it.size,
                compareByDescending { item -> item.second }
            )

            for (item in it) {
                if (item.value.isBlank()) {
                    continue
                }

                // 데이터에 우선순위 부여
                val priority = PriorityRule.personalInfoRule(item.key)
                pq.add(Pair(item, priority))
            }

            // FlexBoxLayout 자식 뷰로 TextInputLayout, EditText 생성
            while (pq.isNotEmpty()) {
                val item = pq.poll() ?: continue

                val til = TextInputLayout(context)
                til.boxBackgroundColor = ContextCompat.getColor(
                    context, android.R.color.transparent
                )
                til.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
                til.setBoxCornerRadii(
                    16f,
                    16f,
                    16f,
                    16f
                )
                til.setPadding(32, 24, 32, 24)
                til.isEnabled = false

                val tie = TextInputEditText(til.context)
                tie.hint = GradeUtils.convertToKorean(item.first.key)
                tie.setAutofillHints(GradeUtils.convertToKorean(item.first.key))
                tie.setText(item.first.value)
                tie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                tie.minWidth = 100 * (tie.hint?.length ?: 1)

                til.addView(tie)

                flexBoxLayout.addView(til)
            }
        }
    }

}