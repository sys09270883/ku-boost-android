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
import com.konkuk.boost.databinding.FragmentLoginBinding
import com.konkuk.boost.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.login()
        }

        viewModel.loginResource.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    runBlocking {
                        delay(1000L)
                        viewModel.clearLoginResource()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
                UseCase.Status.ERROR -> {
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                    Log.e("yoonseop", "${it.message}")
                }
            }
        }
    }


}