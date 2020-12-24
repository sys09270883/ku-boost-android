package com.corgaxm.ku_alarmy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.corgaxm.ku_alarmy.R
import kotlinx.coroutines.*

class SplashFragment : Fragment() {

    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Change to LoginFragment
        coroutineScope.launch {
            delay(2000L)
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onPause() {
        super.onPause()
        coroutineScope.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (!coroutineScope.isActive) {
            coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }
}