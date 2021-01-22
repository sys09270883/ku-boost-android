package com.konkuk.boost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    private fun setSettingsClickListener() {
        binding.apply {
            shareKaKaoLinkBtn.setOnClickListener {
                KaKaoUtils.share(requireContext())
            }
            showOpenSourceBtn.setOnClickListener {

            }
            logoutBtn.setOnClickListener {

            }
        }
    }
}