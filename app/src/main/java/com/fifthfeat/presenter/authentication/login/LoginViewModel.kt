package com.fifthfeat.presenter.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fifthfeat.domain.usecase.authentication.LoginUseCase
import com.fifthfeat.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            loginUseCase(email, password)

            emit(StateView.Success(Unit))
        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

}