package com.konkuk.boost.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.databinding.FragmentChangePasswordBinding
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.ChangePasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChangePasswordViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.isPasswordValid.observe(viewLifecycleOwner) {
            if (viewModel.isFirstPasswordEmpty()) {
                binding.passwordLayout.error = null
            } else {
                when (it) {
                    true -> binding.passwordLayout.error = null
                    else -> binding.passwordLayout.error = "8~20자 이내, 하나 이상의 문자, 숫자, 특수 문자를 입력하세요."
                }
            }
        }

        viewModel.isPasswordSame.observe(viewLifecycleOwner) {
            if (viewModel.isSecondPasswordEmpty()) {
                binding.passwordLayout2.error = null
            } else {
                when (it) {
                    true -> binding.passwordLayout2.error = null
                    false -> binding.passwordLayout2.error = "패스워드를 다시 확인하세요."
                }
            }
        }

        viewModel.changePasswordResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    findNavController().navigate(R.id.action_changePasswordFragment_to_loginFragment)
                }
                UseCase.Status.ERROR -> {
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = requireArguments().getString("username", "")
        val beforePassword = requireArguments().getString("password", "")
        Log.d("yoonseop", username)
        Log.d("yoonseop", beforePassword)
        viewModel.setUsername(username)
        viewModel.setBeforePassword(beforePassword)

        binding.apply {
            confirmButton.setOnClickListener {
                viewModel?.changePassword()
            }

            password.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel?.checkPasswordValid(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            password2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val password = binding.password.text.toString()
                    val password2 = s.toString()
                    viewModel?.updatePasswordState(password, password2)
                }

                override fun afterTextChanged(s: Editable?) {}

            })
        }
    }
}