package com.fifthfeat.presenter.authentication.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fifthfeat.R
import com.fifthfeat.databinding.FragmentRegisterBinding
import com.fifthfeat.util.FirebaseHelper.Companion.getAuth
import com.fifthfeat.util.OauthKey.DEFAULT_WEB_CLIENT_ID
import com.fifthfeat.util.StateView
import com.fifthfeat.util.goToMainNavigation
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
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    private val requestCodeSignIn: Int = 9001
    private var googleSignInClient: GoogleSignInClient? = null

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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener { validateData() }

        binding.btnGoogleRegister.setOnClickListener {
            registerWithGoogle()
        }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        if (email.isEmailValid()) {
            if (password.isNotEmpty()) {
                hideKeyboard()
                register(email, password)
            } else {
                showSnackBar(getString(R.string.text_password_empty))
            }
        } else {
            showSnackBar(getString(R.string.text_email_invalid))
        }
    }

    private fun register(email: String, password: String) {
        viewModel.register(email, password).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressLoading.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressLoading.isVisible = false
                    goToMainNavigation()
                    showSnackBar(getString(R.string.text_register_success_register_fragment))
                }

                is StateView.Error -> {
                    binding.progressLoading.isVisible = false
                    showSnackBar(getString(R.string.error_generic))
                }
            }
        }
    }

    private fun registerWithGoogle() {
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
            showSnackBar(getString(R.string.error_login_google))
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        binding.progressLoading.isVisible = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        getAuth().signInWithCredential(credential)
            .addOnCompleteListener(
                requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    showSnackBar(getString(R.string.text_login_google_success_register_fragment))
                    binding.progressLoading.isVisible = false
                    goToMainNavigation()
                } else {
                    showSnackBar(getString(R.string.error_register_google))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}