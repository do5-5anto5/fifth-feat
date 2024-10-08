package com.fifthfeat.presenter.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fifthfeat.domain.usecase.authentication.RegisterUseCase
import com.fifthfeat.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    fun register(email: String, password: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            registerUseCase.invoke(email, password)

            emit(StateView.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }

}