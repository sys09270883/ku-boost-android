package com.konkuk.boost.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.konkuk.boost.ui.AcademicEventFragment
import com.konkuk.boost.ui.DefaultInfoFragment
import com.konkuk.boost.ui.RegistrationAndScholarshipFragment

class InfoFragmentStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DefaultInfoFragment()
            1 -> AcademicEventFragment()
            2 -> RegistrationAndScholarshipFragment()
            else -> throw Exception("Invalid index in MainFragmentStateAdapter.kt")
        }
    }
}