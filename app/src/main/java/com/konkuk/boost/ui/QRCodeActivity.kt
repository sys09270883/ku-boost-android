package com.konkuk.boost.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.konkuk.boost.R
import com.konkuk.boost.databinding.ActivityQRCodeBinding
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.QRCodeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

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
                    val membershipCard = it.data?.data?.membershipCard ?: return@observe
                    val thumbnailUrl = it.data.data.thumbnailUrl
                    val bitmap = generateQRCode(membershipCard)
                    Glide.with(this).load(thumbnailUrl).into(binding.thumbnailImageView)
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
        val hints = Hashtable<EncodeHintType, ErrorCorrectionLevel>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d("ku-boost", "generateQRCode: ${e.message}")
        }
        return bitmap
    }

}