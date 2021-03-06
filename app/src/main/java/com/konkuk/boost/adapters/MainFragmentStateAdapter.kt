package com.konkuk.boost.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.konkuk.boost.ui.CourseFragment
import com.konkuk.boost.ui.GradeFragment
import com.konkuk.boost.ui.SettingsFragment

class MainFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GradeFragment()
            1 -> CourseFragment()
            2 -> SettingsFragment()
            else -> throw Exception("Invalid index in MainFragmentStateAdapter.kt")
        }
    }
}