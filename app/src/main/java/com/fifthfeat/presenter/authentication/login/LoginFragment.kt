package com.fifthfeat.presenter.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fifthfeat.R
import com.fifthfeat.databinding.FragmentLoginBinding
import com.fifthfeat.util.FirebaseHelper.Companion.getAuth
import com.fifthfeat.util.OauthKey.DEFAULT_WEB_CLIENT_ID
import com.fifthfeat.util.StateView
import com.fifthfeat.util.hideKeyboard
import com.fifthfeat.util.initToolbar
import com.fifthfeat.util.isEmailValid
import com.fifthfeat.util.showSnackBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private val requestCodeSignIn: Int = 9001
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListeners()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun initListeners() {
        with (binding) {
            btnLogin.setOnClickListener {
                validateData()
            }
            btnLoginGoogle.setOnClickListener {
                signInWithGoogle(
                )
            }
            btnForgotPassword.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
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

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, requestCodeSignIn)
    }

    @Deprecated("This method has been deprecated")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken)
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "login failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        getAuth().signInWithCredential(credential)
            .addOnCompleteListener(
                requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "login failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}