package com.fifthfeat.presenter.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.fifthfeat.R
import com.fifthfeat.databinding.FragmentLoginBinding
import com.fifthfeat.util.StateView
import com.fifthfeat.util.hideKeyboard
import com.fifthfeat.util.isEmailValid
import com.fifthfeat.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        with (binding) {
            button.setOnClickListener {
                validateData()
            }
            Glide
                .with(requireContext())
                .load(R.drawable.loading)
                .into(progressLoading)
        }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        if (email.isEmailValid()) {
            if (password.isNotEmpty()) {
                hideKeyboard()
                login(email, password)
            } else {
                showSnackBar(getString(R.string.text_password_empty))
            }
        } else {
            showSnackBar(getString(R.string.text_email_invalid))
        }
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressLoading.isVisible = true
                }
                is StateView.Success -> {
                    binding.progressLoading.isVisible = false
                    showSnackBar("Mock success")
                }
                is StateView.Error -> {
                    binding.progressLoading.isVisible = false
                    showSnackBar("Mock Error")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}