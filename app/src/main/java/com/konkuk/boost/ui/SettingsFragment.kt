package com.konkuk.boost.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.konkuk.boost.R
import com.konkuk.boost.databinding.FragmentSettingsBinding
import com.konkuk.boost.utils.KaKaoUtils
import com.konkuk.boost.viewmodels.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSettingsClickListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.logoutResponse.observe(viewLifecycleOwner) {
            viewModel.clearLogoutResponse()
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }

    private fun setSettingsClickListener() {
        binding.apply {
            mobileQrCodeBtn.setOnClickListener {
                startActivity(Intent(requireActivity(), QRCodeActivity::class.java))
            }

            shareKaKaoLinkBtn.setOnClickListener {
                KaKaoUtils.share(requireContext())
            }

            openSourceBtn.setOnClickListener {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.prompt_opensource_title))
            }

            logoutBtn.setOnClickListener {
                viewModel?.logout()
            }
        }
    }
}