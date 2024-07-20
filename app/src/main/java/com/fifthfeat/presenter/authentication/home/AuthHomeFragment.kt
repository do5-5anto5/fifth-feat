package com.fifthfeat.presenter.authentication.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fifthfeat.R
import com.fifthfeat.databinding.FragmentAuthHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthHomeFragment : Fragment() {
    private var _binding: FragmentAuthHomeBinding? = null
    private val binding get() = _binding!!
    private var listener: GoogleLoginClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GoogleLoginClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnButtonClickedListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    interface GoogleLoginClickListener {
        fun onGoogleLoginClick()
    }

    private fun initListeners() {
        with(binding) {
            btnGoogleLogin.setOnClickListener {
                listener?.onGoogleLoginClick() // Notify the activity about the click
            }
            btnPasswordLogin.setOnClickListener {
                findNavController().navigate(R.id.action_authHomeFragment_to_loginFragment)
            }
            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_authHomeFragment_to_registerFragment)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null // Evita vazamentos de memória
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}