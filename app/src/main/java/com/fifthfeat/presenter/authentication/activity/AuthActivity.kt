package com.fifthfeat.presenter.authentication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fifthfeat.R
import com.fifthfeat.databinding.ActivityAuthBinding
import com.fifthfeat.presenter.authentication.enums.AuthenticationDestinations
import com.fifthfeat.presenter.authentication.enums.AuthenticationDestinations.*
import com.fifthfeat.presenter.main.activity.MainActivity
import com.fifthfeat.util.FirebaseHelper.Companion.getCurrentUser
import com.fifthfeat.util.getSerializableCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        verifyAuth()
        initNavigation()
    }

    private fun initNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
        navController = navHostFragment.navController

        val graph = navController.navInflater.inflate(R.navigation.auth_graph)
        graph.setStartDestination(getDestination())
        navController.graph = graph
    }

    private fun getDestination(): Int {
        val destination =
            intent.getSerializableCompat<AuthenticationDestinations>(AUTHENTICATION_PARAMETER)

        return when (destination) {
            LOGIN_SCREEN -> {
                R.id.authentication
            }

            else -> {
                R.id.splashFragment2
            }
        }
    }

    companion object {
        const val AUTHENTICATION_PARAMETER = "AUTHENTICATION_PARAMETER"
    }

    private fun verifyAuth() {
        if (getCurrentUser()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}