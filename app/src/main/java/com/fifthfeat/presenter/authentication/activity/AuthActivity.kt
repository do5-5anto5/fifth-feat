package com.fifthfeat.presenter.authentication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fifthfeat.databinding.ActivityAuthBinding
import com.fifthfeat.presenter.main.activity.MainActivity
import com.fifthfeat.util.FirebaseHelper.Companion.getCurrentUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(){

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        verifyAuth()
    }

    private fun verifyAuth() {
        if (getCurrentUser()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}