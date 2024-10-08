package com.fifthfeat.presenter.authentication.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fifthfeat.R
import com.fifthfeat.databinding.FragmentForgotBinding
import com.fifthfeat.util.StateView
import com.fifthfeat.util.goToMainNavigation
import com.fifthfeat.util.hideKeyboard
import com.fifthfeat.util.initToolbar
import com.fifthfeat.util.isEmailValid
import com.fifthfeat.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotFragment : Fragment() {
    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForgotViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListeners()
    }

    private fun initListeners() {
        with (binding) {
            btnForgot.setOnClickListener { validateData() }
        }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString()
        if (email.isEmailValid()) {
            hideKeyboard()
            forgot(email)
        } else {
            showSnackBar(getString(R.string.text_email_invalid))
        }
    }

    private fun forgot(email: String) {
        viewModel.forgot(email).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressLoading.isVisible = true
                }
                is StateView.Success -> {
                    binding.progressLoading.isVisible = false
                    showSnackBar(getString(R.string.email_sent_success_forgot_fragment))
                    goToMainNavigation()
                }
                is StateView.Error -> {
                    binding.progressLoading.isVisible = false
                    showSnackBar(getString(R.string.error_generic))
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}