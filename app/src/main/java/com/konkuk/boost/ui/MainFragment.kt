package com.konkuk.boost.ui

import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.konkuk.boost.R
import com.konkuk.boost.adapters.MainFragmentStateAdapter
import com.konkuk.boost.databinding.FragmentMainBinding
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.MainFragmentViewModel
import com.squareup.seismic.ShakeDetector
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val tabTextList = arrayListOf("성적", "수강", "설정")
    private val tabIconList =
        arrayListOf(R.drawable.tab_grade, R.drawable.tab_course, R.drawable.tab_setting)
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainFragmentViewModel by viewModel()
    private var sensorDetector: ShakeDetector? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity()

        binding.apply {
            viewPager.adapter = MainFragmentStateAdapter(activity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val tabView = layoutInflater.inflate(R.layout.custom_tab, null)
                tabView.findViewById<ImageView>(R.id.tabIcon)
                    .setImageDrawable(ContextCompat.getDrawable(activity, tabIconList[position]))
                tabView.findViewById<TextView>(R.id.tabText).text = tabTextList[position]
                tab.customView = tabView
            }.attach()
        }

        viewModel.login()

        viewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                }
                UseCase.Status.ERROR -> {
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setShakeDetector() {
        val sensorManager =
            requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        sensorDetector = ShakeDetector {
            val intent = Intent(requireActivity(), QRCodeActivity::class.java)
            startActivity(intent)
        }
        sensorDetector?.start(sensorManager)
    }

    override fun onResume() {
        super.onResume()
        setShakeDetector()
    }

    override fun onPause() {
        super.onPause()
        sensorDetector?.stop()
        sensorDetector = null
    }

}