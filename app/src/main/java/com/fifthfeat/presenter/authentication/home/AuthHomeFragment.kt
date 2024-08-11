package com.fifthfeat.presenter.authentication.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fifthfeat.R
import com.fifthfeat.databinding.FragmentAuthHomeBinding
import com.fifthfeat.util.FirebaseHelper.Companion.getAuth
import com.fifthfeat.util.OauthKey.DEFAULT_WEB_CLIENT_ID
import com.fifthfeat.util.goToMainNavigation
import com.fifthfeat.util.onNavigate
import com.fifthfeat.util.showSnackBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthHomeFragment : Fragment() {
    private var _binding: FragmentAuthHomeBinding? = null
    private val binding get() = _binding!!

    private val requestCodeSignIn: Int = 9001
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun initListeners() {
        with(binding) {
            btnGoogleLogin.setOnClickListener {
                signInWithGoogle()
            }
            btnPasswordLogin.setOnClickListener {
                findNavController().onNavigate(R.id.action_authHomeFragment_to_loginFragment)
            }
            btnRegister.setOnClickListener {
                findNavController().onNavigate(R.id.action_authHomeFragment_to_registerFragment)
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
                    binding.progressLoading.isVisible = false
                    showSnackBar(getString(R.string.text_login_google_success_onboarding_fragment))
                    goToMainNavigation()
                } else {
                    showSnackBar(getString(R.string.error_login_google))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}