package com.konkuk.boost.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.databinding.ActivityQRCodeBinding
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.QRCodeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QRCodeActivity : AppCompatActivity() {

    private var _binding: ActivityQRCodeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QRCodeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_q_r_code)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.getMobileQRCode()

        viewModel.qrResponse.observe(this) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    Log.d("yoonseop", "data: ${it.data?.data}")
                    Log.d("yoonseop", "membership: ${it.data?.data?.membershipCard}")
                    Log.d("yoonseop", "thumbnail: ${it.data?.data?.thumbnailUrl}")
                }
                UseCase.Status.ERROR -> {
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}