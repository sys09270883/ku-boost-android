package com.konkuk.boost.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
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
                    val membershipCard = it.data?.data?.membershipCard
                    val thumbnailUrl = it.data?.data?.thumbnailUrl
                    val bitmap = generateQRCode("M-${membershipCard}")
                    binding.qrImageView.setImageBitmap(bitmap)
                }
                UseCase.Status.ERROR -> {
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d("yoonseop", "generateQRCode: ${e.message}")
        }
        return bitmap
    }

}