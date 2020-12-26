package com.corgaxm.ku_alarmy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.corgaxm.ku_alarmy.R
import com.corgaxm.ku_alarmy.utils.Resource
import com.corgaxm.ku_alarmy.viewmodels.SplashViewModel
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
                Resource.Status.SUCCESS -> {
                    viewModel.clearLoginResource()
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                }
                Resource.Status.ERROR -> {
                    Log.d("yoonseop", "${it.message}")
                    coroutineScope.launch {
                        delay(2000L)
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        coroutineScope.cancel()
    }
}