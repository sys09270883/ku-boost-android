package com.konkuk.boost.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.databinding.FragmentSplashBinding
import com.konkuk.boost.utils.MessageUtils
import com.konkuk.boost.utils.NetworkUtils
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.SplashViewModel
import com.konkuk.boost.views.DialogUtils
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private val viewModel: SplashViewModel by viewModel()
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLoginResource()
        observeEvent()
        observeUserInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleNetwork()
    }

    private fun handleNetwork() {
        val activity = requireActivity()
        val isConnected = checkNetworkConnected()
        if (isConnected) {
            viewModel.autoLogin()
            viewModel.fetchUserInfo()
        } else {    // 네트워크 미연결시
            val username = viewModel.getUsername()
            val hasLoggedUser = username.isNotBlank()
            if (hasLoggedUser) {    // 사용자 정보가 저장되어 있을 경우
                Snackbar.make(
                    binding.container,
                    getString(R.string.prompt_no_network_alert),
                    Snackbar.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            } else {                // 사용자 정보가 저장되어 있지 않을 경우
                val builder = AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.prompt_no_network))
                    .setCancelable(false)
                    .setPositiveButton(
                        getString(R.string.prompt_yes)
                    ) { _, _ -> activity.finish() }
                val dialog = DialogUtils.recolor(builder.create())
                dialog.show()
            }
        }
    }

    private fun observeEvent() {
        viewModel.eventBit.observe(viewLifecycleOwner) {
            if (it == 0b11) {
                viewModel.clearLoginResource()
                runBlocking {
                    // Wait 0.5 second for saving session.
                    delay(500L)
                    findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                }
            }
        }
    }

    private fun observeLoginResource() {
        viewModel.loginResource.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    viewModel.updateEvent(0b01)
                }
                UseCase.Status.ERROR -> {
                    coroutineScope.launch {
                        delay(1500L)
                        viewModel.clearLoginResource()
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }
        }
    }

    private fun observeUserInfo() {
        viewModel.userInfoResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    viewModel.updateEvent(0b10)
                }
                UseCase.Status.ERROR -> {
                    Log.e(MessageUtils.LOG_KEY, "${it.message}")
                }
            }
        }
    }

    private fun checkNetworkConnected(): Boolean =
        NetworkUtils.getConnectivityStatus(requireContext())

    override fun onPause() {
        super.onPause()
        coroutineScope.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}