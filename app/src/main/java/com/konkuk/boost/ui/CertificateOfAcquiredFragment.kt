package com.konkuk.boost.ui

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.konkuk.boost.databinding.FragmentCertificateOfAcquiredBinding
import com.konkuk.boost.utils.HtmlEngine
import com.konkuk.boost.viewmodels.CertificateOfAcquiredViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CertificateOfAcquiredFragment : Fragment() {

    private var _binding: FragmentCertificateOfAcquiredBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CertificateOfAcquiredViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateOfAcquiredBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.certificateWebView.apply {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                when (resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
                    UI_MODE_NIGHT_YES -> WebSettingsCompat.setForceDark(
                        settings,
                        WebSettingsCompat.FORCE_DARK_ON
                    )
                    else -> WebSettingsCompat.setForceDark(
                        settings,
                        WebSettingsCompat.FORCE_DARK_OFF
                    )
                }
            }

            val engine = HtmlEngine.getCertificateOfAcquired(resources.assets, viewModel.getStdNo())
            settings.javaScriptEnabled = true
            loadData(engine, null, null)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}