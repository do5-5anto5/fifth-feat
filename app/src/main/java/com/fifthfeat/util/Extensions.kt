package com.fifthfeat.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.fifthfeat.R
import com.fifthfeat.databinding.BottomSheetLogoutBinding
import com.fifthfeat.presenter.authentication.activity.AuthActivity
import com.fifthfeat.presenter.authentication.activity.AuthActivity.Companion.AUTHENTICATION_PARAMETER
import com.fifthfeat.presenter.authentication.enums.AuthenticationDestinations
import com.fifthfeat.presenter.main.activity.MainActivity
import com.fifthfeat.util.FirebaseHelper.Companion.signOut
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

fun Fragment.hideKeyboard() {
    val view = activity?.currentFocus
    if (view != null) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}

fun Fragment.showSnackBar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT
) {
    view?.let { Snackbar.make(it, message, duration).show() }
}

fun String.isEmailValid(): Boolean {
    val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    return emailPattern.matches(this)
}

fun Fragment.initToolbar(toolbar: Toolbar, showBackIcon: Boolean = true) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

    toolbar.setNavigationOnClickListener {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}

fun Fragment.goToMainNavigation() {
    Handler(Looper.getMainLooper()).postDelayed(
        {
            run {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }, 2500
    )
}

fun Fragment.showBottomSheetLogout(destination: AuthenticationDestinations) {
    val bottomSheetLogout = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val bottomSheetBinding = BottomSheetLogoutBinding.inflate(layoutInflater, null, false)

    bottomSheetBinding.btnCancel.setOnClickListener {
        bottomSheetLogout.dismiss()
    }

    bottomSheetBinding.btnConfirm.setOnClickListener {
        logout(destination)
        bottomSheetLogout.dismiss()
    }

    bottomSheetLogout.setContentView(bottomSheetBinding.root)
    bottomSheetLogout.show()
}

fun Fragment.logout(destination: AuthenticationDestinations) {
    signOut()
    requireActivity().finish()
    val intent = Intent(requireContext(), AuthActivity::class.java)
    intent.putExtra(AUTHENTICATION_PARAMETER, destination)
    startActivity(intent)
}

inline fun <reified T : Serializable>
        Intent.getSerializableCompat(key: String): T? = when {

    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
        getSerializableExtra(
            key,
            T::class.java
        )
    else -> getSerializableExtra(key) as? T
}

fun NavController.onNavigate(action: Int) {
    this.navigate(
        action,
        null,
        NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.pop_enter)
            .setPopExitAnim(R.anim.pop_exit)
            .build()
    )
}

fun NavController.onNavigate(nav: NavDirections) {
    this.navigate(
        nav.actionId,
        nav.arguments,
        NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setPopEnterAnim(R.anim.pop_enter)
            .setExitAnim(R.anim.exit)
            .setPopExitAnim(R.anim.pop_exit)
            .build()
    )
}
