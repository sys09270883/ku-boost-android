package com.konkuk.boost.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.konkuk.boost.R
import com.konkuk.boost.data.UseCase
import com.konkuk.boost.viewmodels.SplashViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginResource.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    viewModel.clearLoginResource()
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                }
                UseCase.Status.ERROR -> {
                    Log.d("yoonseop", "${it.message}")
                    coroutineScope.launch {
                        delay(1500L)
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }
        }

        viewModel.autoLogin()
    }

    override fun onPause() {
        super.onPause()
        coroutineScope.cancel()
    }
}