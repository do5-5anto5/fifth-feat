package com.fifthfeat.presenter.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.fifthfeat.R
import com.fifthfeat.databinding.FragmentRegisterBinding
import com.fifthfeat.util.StateView
import com.fifthfeat.util.hideKeyboard
import com.fifthfeat.util.initToolbar
import com.fifthfeat.util.isEmailValid
import com.fifthfeat.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListeners()
    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener { validateData() }

        Glide
            .with(requireContext())
            .load(R.drawable.loading)
            .into(binding.progressLoading)
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        if (email.isEmailValid()) {
            if (password.isNotEmpty()) {
                hideKeyboard()
                register(email, password)
            }
            else {
                showSnackBar(getString(R.string.text_password_empty))
            }
        } else {
            showSnackBar(getString(R.string.text_email_invalid))
        }
    }

    private fun register(email: String, password: String){
        viewModel.register(email, password).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressLoading.isVisible = true
                }
                is StateView.Success -> {
                    binding.progressLoading.isVisible = false
                    showSnackBar("Mock success")
                }
                is StateView.Error -> {
                    binding.progressLoading.isVisible = true
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