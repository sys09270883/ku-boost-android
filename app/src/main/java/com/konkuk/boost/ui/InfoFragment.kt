package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.konkuk.boost.adapters.InfoFragmentStateAdapter
import com.konkuk.boost.databinding.FragmentInfoBinding
import com.konkuk.boost.viewmodels.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InfoViewModel by viewModel()
    private val tabTextList = arrayListOf("기본정보", "학적변동", "등록/장학")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity()

        binding.apply {
            viewPager.adapter = InfoFragmentStateAdapter(activity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTextList[position]
            }.attach()
        }
    }
}