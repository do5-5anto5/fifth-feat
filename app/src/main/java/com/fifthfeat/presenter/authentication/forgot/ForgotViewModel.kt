package com.fifthfeat.presenter.authentication.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fifthfeat.domain.usecase.authentication.ForgotUseCase
import com.fifthfeat.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ForgotViewModel @Inject constructor(
    private val forgotUseCase: ForgotUseCase
) : ViewModel() {

    fun forgot(email: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            forgotUseCase(email)

            emit(StateView.Success(Unit))
        } catch (e: Exception) {
            emit(StateView.Error(e.message))
        }
    }

}