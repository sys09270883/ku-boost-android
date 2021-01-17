package com.konkuk.boost.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.konkuk.boost.ui.CourseFragment
import com.konkuk.boost.ui.GradeFragment
import com.konkuk.boost.ui.SettingsFragment

class MainFragmentStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    // 수강 탭, 성적 탭, 설정 탭
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CourseFragment()
            1 -> GradeFragment()
            2 -> SettingsFragment()
            else -> throw Exception("Invalid index in MainFragmentStateAdapter.kt")
        }
    }
}