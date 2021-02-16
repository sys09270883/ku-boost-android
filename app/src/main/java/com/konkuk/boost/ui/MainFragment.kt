package com.konkuk.boost.ui

import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.konkuk.boost.R
import com.konkuk.boost.adapters.MainFragmentStateAdapter
import com.konkuk.boost.databinding.FragmentMainBinding
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.MainFragmentViewModel
import com.konkuk.boost.views.DialogUtils.setProgressBar
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
    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewPager.adapter = MainFragmentStateAdapter(this@MainFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setIcon(tabIconList[position])
                tab.text = tabTextList[position]
            }.attach()
        }

        fetchData()
    }

    private fun fetchData() {
        if (viewModel.isNotFetched()) {
            viewModel.fetchStudentInfo()
            viewModel.fetchValidGradesAndUpdateClassificationFromServer()
            viewModel.fetchGraduationSimulationFromServer()
            viewModel.makeTotalRank()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.libraryLogin()

        viewModel.libraryLoginResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                }
                UseCase.Status.ERROR -> {
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        observeLoading()
    }

    private fun observeLoading() {
        viewModel.allGradesLoading.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    if (!viewModel.hasData()) {
                        // 첫 로그인 시 로컬 데이터베이스가 비어있는 경우 다이얼로그를 띄움
                        val context = requireContext()
                        val builder = AlertDialog.Builder(context)
                        dialog = builder
                            .setTitle(getString(R.string.app_name))
                            .setMessage(getString(R.string.prompt_chart_no_data))
                            .setProgressBar(ProgressBar(context))
                            .setCancelable(false)
                            .create()
                        dialog?.show()
                    }
                }
                false -> {
                    try {
                        if (dialog?.isShowing == true) dialog?.dismiss()
                    } catch (e: Exception) {
                        Log.e("ku-boost", "${e.message}")
                    }
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