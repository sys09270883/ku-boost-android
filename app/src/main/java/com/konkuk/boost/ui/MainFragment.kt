package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.konkuk.boost.adapters.MainFragmentStateAdapter
import com.konkuk.boost.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val tabTextList = arrayListOf("수강", "성적", "설정")
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity()

        binding.apply {
            viewPager.adapter = MainFragmentStateAdapter(activity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTextList[position]
            }.attach()
        }
    }

}