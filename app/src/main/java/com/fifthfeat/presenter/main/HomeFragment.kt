package com.fifthfeat.presenter.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fifthfeat.databinding.FragmentHomeBinding
import com.fifthfeat.presenter.authentication.enums.AuthenticationDestinations.LOGIN_SCREEN
import com.fifthfeat.util.showBottomSheetLogout

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.btnLogout.setOnClickListener {
            showBottomSheetLogout(LOGIN_SCREEN)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}