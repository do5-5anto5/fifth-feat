package com.fifthfeat.domain.usecase.authentication

import com.fifthfeat.domain.repository.FireBaseAuthentication
import javax.inject.Inject

class ForgotUseCase @Inject constructor(
    private val firebaseAuthentication : FireBaseAuthentication
) {

    suspend operator fun invoke(email: String) {
        firebaseAuthentication.forgot(email)
    }

}