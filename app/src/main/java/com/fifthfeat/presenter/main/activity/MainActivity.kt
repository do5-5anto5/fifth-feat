package com.fifthfeat.presenter.main.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.fifthfeat.R
import com.fifthfeat.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomBar, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomBar.isVisible =
                destination.id == R.id.menu_home ||
                destination.id == R.id.menu_orders ||
                destination.id == R.id.menu_cart ||
                destination.id == R.id.menu_profile
        }
    }
}