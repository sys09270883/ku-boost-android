package com.konkuk.boost.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.konkuk.boost.R
import com.konkuk.boost.databinding.FragmentLoginBinding
import com.konkuk.boost.utils.MessageUtils
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.viewmodels.LoginViewModel
import com.konkuk.boost.views.DialogUtils
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

    private fun login() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.username.windowToken, 0)
        imm.hideSoftInputFromWindow(binding.password.windowToken, 0)
        viewModel.login()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            login()
        }

        binding.password.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    login()
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLoginResource()
        observeChangePasswordResponse()
        observeEvent()
        observeUserInfo()
    }

    private fun observeEvent() {
        viewModel.eventBit.observe(viewLifecycleOwner) {
            if (it == 0b11) {
                viewModel.loading.postValue(false)
                viewModel.clearLoginResource()
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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

    private fun observeChangePasswordResponse() {
        viewModel.changePasswordResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    val flag = it.data?.response?.flag
                    if (flag == "PASS") {
                        viewModel.login()
                    }
                }
                UseCase.Status.ERROR -> {
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeLoginResource() {
        viewModel.loginResource.observe(viewLifecycleOwner) {
            when (it.status) {
                UseCase.Status.SUCCESS -> {
                    viewModel.fetchUserInfo()
                    viewModel.updateEvent(0b01)
                }
                UseCase.Status.ERROR -> {
                    if (it.data != null) {
                        if (it.data.loginFailure?.errorCode == "SYS.CMMN@CMMN018") {
                            viewModel.clearLoginResource()
                            val context = requireContext()
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle(getString(R.string.app_name))
                                .setMessage("비밀번호 변경 후 90일이 지났습니다. 비밀번호를 변경해주세요.")
                                .setCancelable(false)
                                .setPositiveButton("90일 뒤 변경") { _, _ ->
                                    viewModel.changePasswordAfter90Days()
                                }
                                .setNegativeButton("비밀번호 변경") { _, _ ->
                                    val bundle = bundleOf(
                                        "username" to binding.username.text.toString(),
                                        "password" to binding.password.text.toString(),
                                        "isLoggedIn" to false
                                    )
                                    findNavController().navigate(
                                        R.id.action_loginFragment_to_changePasswordFragment,
                                        bundle
                                    )
                                }
                            val dialog = DialogUtils.recolor(builder.create())
                            dialog.show()
                        }
                    }
                    Snackbar.make(binding.container, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}